package com.junting.drug_android_frontend

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.junting.drug_android_frontend.databinding.ActivityDrugRecordBinding
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.drug_record.InteractingDrug
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import com.junting.drug_android_frontend.ui.libs.ExpandableListUtils
import java.util.*
import com.junting.drug_android_frontend.ui.drugRecords.DrugRecordsViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DrugRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrugRecordBinding

    internal var adapter: RrugRecordExpandableListAdapter? = null

    private var viewModel: DrugRecordsViewModel = DrugRecordsViewModel()

    private var checkBoxes: Array<CheckBox> = arrayOf()

    private var drugRecordId: Int? = null

    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val today = Calendar.getInstance().time



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrugRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        checkBoxes = arrayOf<CheckBox>(
            binding.cbBeforeMeal,
            binding.cbAfterMeal,
            binding.cbWithFood,
            binding.cbBeforeSleep
        )


        DialogUtils.initTextViewEditDialog(this,binding.llDrugName, binding.tvDrugName,resources.getString(R.string.modify_drug_name),false) { text ->
            viewModel.setDrugName(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llHospital, binding.tvHospital, resources.getString(R.string.modify_hospital_name),false){
            text -> viewModel.setHospitalName(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llDepartment, binding.tvDepartment, resources.getString(R.string.modify_department_name),false){
            text -> viewModel.setHospitalDepartment(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llPhoneExtension, binding.tvPhoneExtension, resources.getString(R.string.modify_phone),true){
            text -> val parts = text.split("-")
            viewModel.setHospitalPhone(parts[0])
            viewModel.setExtension(parts[1])
        }
        DialogUtils.initTextViewEditDialog(this,binding.llIndication, binding.tvIndication, resources.getString(R.string.modify_indication),false){
            text -> viewModel.setIndication(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llIndicationTag, binding.tvIndicationTag, resources.getString(R.string.modify_indication_tag),false){
                text -> viewModel.setIndicationTag(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llSideEffect, binding.tvSideEffect, resources.getString(R.string.modify_side_effect),false){
            text -> viewModel.setSideEffect(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llAppearance, binding.tvAppearance, resources.getString(R.string.modify_appearance),false){
            text -> viewModel.setAppearance(text)
        }
        DialogUtils.initTextViewEditDialog(this,binding.llDosage, binding.tvDosage, resources.getString(R.string.modify_dosage),true){
            text -> viewModel.setDosage(text.toInt())
        }
        DialogUtils.initTextViewEditDialog(this,binding.llStock, binding.tvStock, resources.getString(R.string.modify_stock),true){
            text -> viewModel.setStock(text.toInt())
        }
        initOndemandCheckbox()
        initTimingsCheckbox()
//        initButtonSheet(binding.llNotificationSetting, NotificationSettingButtonSheet(viewModel), "notificationSetting")
        initButtonSheet(binding.llReturnSetting, ReturnSettingButtonSheet(viewModel), "returnSetting")
        initButtonSheet(binding.llDrugPosition, DrugPositionButtonSheet(viewModel), "drugPosition")
        initButton()
        initPhoneLongClickCall()
        showDatePickerDialog()


        drugRecordId = intent.getIntExtra("drugRecordId", 0)
        if (drugRecordId == 0) {
            getObjFromPreviousActivity()  //代表前一個動作並非點選卡片
        } else {
            initDrugRecordViewModel()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_delete -> {
                // 处理点击删除按钮的逻辑
                val alertDialog = MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.window_question_title))
                    .setMessage(resources.getString(R.string.drug_record_activity_question_message))
                    .setPositiveButton(resources.getString(R.string.confirm)) { dialog, _ ->
                        // 在這裡執行刪除操作
                        val drugRecordId = intent.getIntExtra("drugRecordId", 0)
                        if (drugRecordId != 0) {
                            // 呼叫 ViewModel 的刪除方法
                            viewModel.deleteDrugRecordById(drugRecordId)
                        }


                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("fragmentName", "DrugRecordsFragment")
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, _ ->
                        dialog.dismiss()

                    }
                    .create()
                alertDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)

        // 根據條件控制菜單項目的顯示與隱藏
        val drugRecordId = intent.getIntExtra("drugRecordId", 0)
        val deleteMenuItem = menu?.findItem(R.id.action_delete)
        deleteMenuItem?.isVisible = drugRecordId != 0

        return true
    }
    private fun initPhoneLongClickCall() {
        binding.llPhoneExtension.setOnLongClickListener {
            val phone = viewModel.record.value?.hospital?.phone
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
            true
        }
    }

    private fun initButtonSheet(layout: ViewGroup, buttonSheet: BottomSheetDialogFragment, tag: String) {
        layout.setOnClickListener{
            buttonSheet.show(supportFragmentManager, tag)
        }
    }
    private fun getObjFromPreviousActivity(){
        viewModel.record.value = DrugRecord()

        if (intent.getSerializableExtra("drugInteractions") != null) {
            val drugInteractions = intent.getSerializableExtra("drugInteractions") as List<InteractingDrug>
//            initExpandableListInteraction(drugInteractions)
            Log.d("InteractingDrugs", "drugInteractions: ${drugInteractions}")

            viewModel.setInteractingDrugs(drugInteractions)
            initExpandableListInteraction(drugInteractions)
        }
        if (intent.getSerializableExtra("drugbagInfo") != null) {
            val drugbagInfo = intent.getSerializableExtra("drugbagInfo") as DrugbagInformation
            Log.d("DrugbagInformation", "drugbagInfo: ${drugbagInfo}")

            viewModel.setDrugName(drugbagInfo.drug.name)
            viewModel.setHospitalName(drugbagInfo.hospital.name)
            viewModel.setHospitalDepartment(drugbagInfo.hospital.department)
            viewModel.setHospitalPhone(drugbagInfo.hospital.phone)
            viewModel.setIndication(drugbagInfo.drug.indication)
            viewModel.setSideEffect(drugbagInfo.drug.sideEffect)
            viewModel.setAppearance(drugbagInfo.drug.appearance)
            viewModel.setOnDemand(drugbagInfo.onDemand)
            var timeSlots = mutableListOf<String>()
            when(drugbagInfo.frequency){
                1 -> timeSlots = mutableListOf("08:00")
                2 -> timeSlots = mutableListOf("08:00", "12:00")
                3 -> timeSlots = mutableListOf("08:00", "12:00", "18:00")
                4 -> timeSlots = mutableListOf("08:00", "12:00", "18:00", "22:00")
                5 -> timeSlots = mutableListOf("08:00", "12:00", "18:00", "22:00", "24:00")
            }
            viewModel.setTimeSlots(timeSlots)
            initTimeSection(timeSlots)

            viewModel.setTimings(drugbagInfo.timings)
            for (i in drugbagInfo.timings) {
                checkBoxes[i].isChecked = true
            }
            viewModel.setDosage(drugbagInfo.dosage)
            viewModel.setStock(drugbagInfo.stock)

            val formattedToday = dateFormat.format(today)
            viewModel.setNotificationSettingStartDate(formattedToday)  //預設開始日期為今天
        }
    }

    private fun initDrugRecordViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchRecordById(drugRecordId!!)
        viewModel.record.observe(this, Observer {
            Log.d("Observe DrugRecord", "record: ${it.toString()}")
            var timeSlots = it.timeSlots.toMutableList()
            initTimeSection(timeSlots)
            initExpandableListInteraction(it.interactingDrugs!!)
            for (i in it.timings) {
                checkBoxes[i].isChecked = true
            }

            val startDate = dateFormat.parse(viewModel.record.value?.notificationSetting?.startDate)
            if (startDate.before(today) || startDate.compareTo(today) <= 0) {
                // startDate 早于或等于今天
                binding.llDate.setOnClickListener(null)  //取消點擊事件
            }

            binding.progressBar.visibility = GONE
        })
    }

    private fun initTimeSection(timeSlots : MutableList<String>) {

        binding.llTimeSlot.removeAllViews() // Remove all existing views before adding new ones

        for (i in timeSlots.indices) {
            val tvTimeSlot = binding.llTimeSlot.getChildAt(i) as? AppCompatTextView
                ?: AppCompatTextView(this)

            tvTimeSlot.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
//            tvTimeSlot.background = ContextCompat.getDrawable(this, R.attr.selectableItemBackground)
            val iconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_outline_delete_24)
            tvTimeSlot.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                iconDrawable,
                null
            )
            tvTimeSlot.setPadding(10, 10, 10, 10)
            tvTimeSlot.text = timeSlots[i]
//            tvTimeSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            tvTimeSlot.setOnClickListener {
                timeSlots.removeAt(i)
                binding.llTimeSlot.removeView(tvTimeSlot)
                viewModel.setTimeSlots(timeSlots)
            }

            if (tvTimeSlot.parent == null) {
                binding.llTimeSlot.addView(tvTimeSlot)
            }
        }
        binding.tvTimeSlotAdd.setOnClickListener {
            val timeList = mutableListOf<String>()
            for (i in 0..23) {
                timeList.add("${i.toString().padStart(2, '0')}:00")
            }

            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle(resources.getString(R.string.drug_record_select_time))
            builder.setItems(timeList.toTypedArray()) { dialog, which ->
                val time = timeList[which]

                // check if the time already exists
                if (timeSlots.contains(time)) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.warning_window_title))
                        .setMessage(resources.getString(R.string.drug_record_activity_warning_message))
                        .setPositiveButton(resources.getString(R.string.confirm), null)
                        .show()
                    return@setItems
                }

                // add the new time to the list
                timeSlots.add(time)

                // sort the time list
                Collections.sort(timeSlots)

                // update the time slots in the view model
                viewModel.setTimeSlots(timeSlots)

                // remove all the views in the LinearLayout
                binding.llTimeSlot.removeAllViews()

                // add all the time slots again to the LinearLayout
                for (timeSlot in timeSlots) {
                    val tvTimeSlot = AppCompatTextView(this)
                    tvTimeSlot.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val iconDrawable =
                        ContextCompat.getDrawable(this, R.drawable.ic_outline_delete_24)
                    tvTimeSlot.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null,
                        null,
                        iconDrawable,
                        null
                    )
                    tvTimeSlot.setPadding(10, 10, 10, 10)
                    tvTimeSlot.text = timeSlot
//                    tvTimeSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    binding.llTimeSlot.addView(tvTimeSlot)
                    tvTimeSlot.setOnClickListener {
                        timeSlots.remove(timeSlot)
                        binding.llTimeSlot.removeView(tvTimeSlot)
                    }
                }
            }
            builder.show()

        }
    }



    private fun initTimingsCheckbox() {
        binding.cbBeforeMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.record.value?.let {
                if (isChecked) {
                    // 新增 0 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(0) }
                } else {
                    // 移除 timings 列表中的 0
                    it.timings = it.timings.toMutableSet().apply { remove(0) }
                }
            }
            binding.cbAfterMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbAfterMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.record.value?.let {
                if (isChecked) {
                    // 新增 1 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(1) }
                } else {
                    // 移除 timings 列表中的 1
                    it.timings = it.timings.toMutableSet().apply { remove(1) }
                }
            }
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbWithFood.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.record.value?.let {
                if (isChecked) {
                    // 新增 2 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(2) }
                } else {
                    // 移除 timings 列表中的 2
                    it.timings = it.timings.toMutableSet().apply { remove(2) }
                }
            }
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbAfterMeal.isEnabled = !isChecked
        }
        binding.cbBeforeSleep.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.record.value?.let {
                if (isChecked) {
                    // 新增 3 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(3) }
                } else {
                    // 移除 timings 列表中的 3
                    it.timings = it.timings.toMutableSet().apply { remove(3) }
                }
            }
        }
    }

    private fun initOndemandCheckbox() {
        binding.llOnDemand.setOnClickListener {
            binding.cbOnDemand.isChecked = !binding.cbOnDemand.isChecked
        }
        binding.cbOnDemand.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.llOuterborderTimeSlot.visibility = GONE
                binding.llTimings.visibility = GONE
                binding.llReturnSetting.visibility = GONE
                binding.llDate.visibility = GONE
                viewModel.setOnDemand(true)
            } else {
                binding.llOuterborderTimeSlot.visibility = View.VISIBLE
                binding.llTimings.visibility = View.VISIBLE
                binding.llReturnSetting.visibility = View.VISIBLE
                binding.llDate.visibility = View.VISIBLE
                viewModel.setOnDemand(false)
            }
        }
    }

    private fun initExpandableListInteraction(interactingDrugs: List<InteractingDrug>) {
        adapter = RrugRecordExpandableListAdapter(this, interactingDrugs)
        if (interactingDrugs.isEmpty()) {
            binding.expandableListInteraction!!.visibility = GONE
        }
        binding.expandableListInteraction!!.setAdapter(adapter)
        ExpandableListUtils.setupExpandHeight(binding.expandableListInteraction!!, adapter!!)
    }
    private fun showDatePickerDialog() {
        binding.llDate.setOnClickListener(View.OnClickListener {
            // 使用 MaterialDatePicker 顯示日期選擇對話框
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            // 設置日期範圍限制，禁止用戶選擇今天以前的日期
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        return date >= today
                    }

                    override fun writeToParcel(dest: Parcel, flags: Int) {
                        // Not needed for this example
                    }

                    override fun describeContents(): Int {
                        return 0
                    }
                })
                .build()


            val builder = MaterialDatePicker.Builder.datePicker()

            val picker = builder
                .setTitleText(resources.getString(R.string.select_start_date_for_medication))
                .setPositiveButtonText(resources.getString(R.string.confirm))
                .setNegativeButtonText(resources.getString(R.string.dialog_cancel))
                .setCalendarConstraints(constraintsBuilder)  // 使用上述的日期範圍限制
                .build()

            // 設置選擇日期後的回調
            picker.addOnPositiveButtonClickListener { selection ->
                // 取得選擇的日期
                val selectedDate = Date(selection)

                // 格式化日期
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)

                // 更新 tv_start_date 的文字
//            binding.tvStartDate.text = formattedDate
                viewModel.setNotificationSettingStartDate(formattedDate)
            }

            // 顯示 MaterialDatePicker 對話框
            picker.show(supportFragmentManager, picker.toString())
        })

    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        binding.btnConfirm.setOnClickListener {
            Log.d("DrugRecord",viewModel.record.value.toString())
            val dialogMessage = when {
                viewModel.record.value?.drug?.name.isNullOrEmpty() -> "藥物名稱不可為空"
                viewModel.record.value?.hospital?.name.isNullOrEmpty() -> "醫院名稱不可為空"
                viewModel.record.value?.hospital?.department.isNullOrEmpty() -> "科別名稱不可為空"
                viewModel.record.value?.timeSlots?.isEmpty() == true -> "吃藥時間不可為空"
                viewModel.record.value?.dosage == 0 -> "劑量不可為0或空"
                viewModel.record.value?.stock!! < (viewModel.record.value?.dosage)!!.times((viewModel.record.value?.frequency!!)) -> "藥袋數量不足，請確認藥袋數量是否足夠"
                viewModel.record.value?.position == 0 -> "藥盒放置位置不可為空"
                else -> "OK"
            }

            if(dialogMessage != "OK"){
                val builder = MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.warning_window_title))
                    .setMessage(dialogMessage)
                    .setPositiveButton(resources.getString(R.string.confirm)) { dialog, which ->
                        // Handle positive button click

                    }
                    .create()

                builder.show()
            }else{
                drugRecordId = intent.getIntExtra("drugRecordId", 0)
                if (drugRecordId == 0) {
                    // 新增 DrugRecord 的操作
                    val drugRecord = viewModel.record.value
                    if (drugRecord != null) {
                        viewModel.addDrugRecord(drugRecord)
                    }
                } else {
                    // 修改 DrugRecord 的操作
                    val drugRecord = viewModel.record.value
                    if (drugRecord != null) {
                        viewModel.updateDrugRecordById(drugRecordId!!, drugRecord)
                    }
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("fragmentName", "DrugRecordsFragment")
                startActivity(intent)
            }
        }
    }
}
