package com.junting.drug_android_frontend

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.junting.drug_android_frontend.databinding.ActivityDrugRecordBinding
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.drug_record.InteractingDrug
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import com.junting.drug_android_frontend.ui.libs.ExpandableListUtils
import java.util.*
import com.junting.drug_android_frontend.ui.drugRecords.DrugRecordsViewModel

class DrugRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrugRecordBinding

    internal var adapter: RrugRecordExpandableListAdapter? = null

    private var viewModel: DrugRecordsViewModel = DrugRecordsViewModel()

    private var checkBoxes: Array<CheckBox> = arrayOf()

    private var drugRecordId: Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrugRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        checkBoxes = arrayOf<CheckBox>(
            binding.cbAfterMeal,
            binding.cbAfterMeal,
            binding.cbWithFood,
            binding.cbBeforeSleep
        )


        initTextViewEditDialog(binding.llDrugName, binding.tvDrugName,"修改藥物名稱",false)
        initTextViewEditDialog(binding.llHospital, binding.tvHospital, "修改醫院名稱",false)
        initTextViewEditDialog(binding.llDepartment, binding.tvDepartment, "修改科別名稱",false)
        initTextViewEditDialog(binding.llIndication, binding.tvIndication, "修改適應症",false)
        initTextViewEditDialog(binding.llSideEffect, binding.tvSideEffect, "修改副作用",false)
        initTextViewEditDialog(binding.llAppearance, binding.tvAppearance, "修改外觀",false)
        initTextViewEditDialog(binding.llDosage, binding.tvDosage, "修改劑量",true)
        initTextViewEditDialog(binding.llStock, binding.tvStock, "修改庫存",true)
        initOndemandCheckbox()
        initTimingsCheckbox()
        initButtonSheet(binding.llNotificationSetting, NotificationSettingButtonSheet(), "notificationSetting")
        initButtonSheet(binding.llDrugPosition, DrugPositionButtonSheet(), "drugPosition")
        initButton()
        //代表前一個動作一點選卡片
        drugRecordId = intent.getIntExtra("drugRecordId", 0)
        if (drugRecordId == 0) {
            getObjFromPreviousActivity()
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
                Toast.makeText(this, "你點了刪除", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_drug_record, menu)

        // 根據條件控制菜單項目的顯示與隱藏
        val drugRecordId = intent.getIntExtra("drugRecordId", 0)
        val deleteMenuItem = menu?.findItem(R.id.action_delete)
        deleteMenuItem?.isVisible = drugRecordId != 0

        return true
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
            viewModel.setHospitalName(drugbagInfo.hospitalName)
            viewModel.setHospitalDepartment(drugbagInfo.hospitalDepartment)
            viewModel.setIndication(drugbagInfo.drug.indication)
            viewModel.setSideEffect(drugbagInfo.drug.sideEffect)
            viewModel.setAppearance(drugbagInfo.drug.appearance)
            viewModel.setOnDemand(drugbagInfo.onDemand)
            var timeSlots = mutableListOf<String>()
            when(drugbagInfo.frequency){
                1 -> timeSlots = mutableListOf("8:00")
                2 -> timeSlots = mutableListOf("8:00", "12:00")
                3 -> timeSlots = mutableListOf("8:00", "12:00", "18:00")
                4 -> timeSlots = mutableListOf("8:00", "12:00", "18:00", "22:00")
                5 -> timeSlots = mutableListOf("8:00", "12:00", "18:00", "22:00", "24:00")
            }
            viewModel.setTimeSlots(timeSlots)
            initTimeSection(timeSlots)

            viewModel.setTimings(drugbagInfo.timings)
            for (i in drugbagInfo.timings) {
                checkBoxes[i].isChecked = true
            }
            viewModel.setDosage(drugbagInfo.dosage)
            viewModel.setStock(drugbagInfo.stock)
        }
    }

    private fun initDrugRecordViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchRecord(drugRecordId!!)
        viewModel.record.observe(this, Observer {
            Log.d("Observe DrugRecord", "record: ${it.toString()}")

            var timeSlots = it.timeSlots.toMutableList()
            viewModel.setTimeSlots(timeSlots)
            initTimeSection(timeSlots)

            initExpandableListInteraction(it.interactingDrugs!!)
            viewModel.setInteractingDrugs(it.interactingDrugs!!)

            for (i in it.timings) {
                checkBoxes[i].isChecked = true
            }
            viewModel.setTimings(it.timings)

            binding.progressBar.visibility = GONE
        })
    }

    private fun initTimeSection(timeSlots : MutableList<String>) {

        for (timeSlot in timeSlots) {
            val tvTimeSlot = AppCompatTextView(this)
            tvTimeSlot.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
//            tvTimeSlot.background = ContextCompat.getDrawable(this, android.R.attr.selectableItemBackground)
            val iconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_outline_delete_24)
            tvTimeSlot.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                iconDrawable,
                null
            )
            tvTimeSlot.setPadding(10, 10, 10, 10)
            tvTimeSlot.text = timeSlot
            tvTimeSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            binding.llTimeSlot.addView(tvTimeSlot)
            tvTimeSlot.setOnClickListener {
                timeSlots.remove(timeSlot)
                binding.llTimeSlot.removeView(tvTimeSlot)
                viewModel.setTimeSlots(timeSlots)
            }
        }
        binding.tvTimeSlotAdd.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .build()

            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour.toString().padStart(2, '0')
                val minute = picker.minute.toString().padStart(2, '0')
                val time = "$hour:$minute"

                // check if the time already exists
                if (timeSlots.contains(time)) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("警告")
                        .setMessage("所選時間已存在。")
                        .setPositiveButton("確定", null)
                        .show()
                    return@addOnPositiveButtonClickListener
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
                    tvTimeSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    binding.llTimeSlot.addView(tvTimeSlot)
                    tvTimeSlot.setOnClickListener {
                        timeSlots.remove(timeSlot)
                        binding.llTimeSlot.removeView(tvTimeSlot)
                    }
                }
            }

            picker.show(supportFragmentManager, "time_picker")
        }
    }

    private fun initTextViewEditDialog(onclickLayout:View,tv: TextView, title: String,onlyDigitInput: Boolean) {
        onclickLayout.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle(title)

            // 建立一個 EditText 供使用者輸入新的藥物名稱
            val input = EditText(this)
            input.setText(tv.text)
            if (onlyDigitInput) {
                input.inputType = InputType.TYPE_CLASS_NUMBER
            }
            builder.setView(input)

            // 設定確認和取消按鈕
            builder.setPositiveButton("確定") { dialog, which ->
                tv.text = input.text.toString()
            }
            builder.setNegativeButton("取消") { dialog, which ->
                dialog.cancel()
            }

            // 顯示對話框
            builder.show()
        }
    }

    private fun initTimingsCheckbox() {
        binding.cbBeforeMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbAfterMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbAfterMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbWithFood.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbAfterMeal.isEnabled = !isChecked
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
            } else {
                binding.llOuterborderTimeSlot.visibility = View.VISIBLE
                binding.llTimings.visibility = View.VISIBLE
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

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            startActivity(intent)
        }
        binding.btnConfirm.setOnClickListener {
            Log.d("DrugRecord",viewModel.record.value.toString())
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            startActivity(intent)
        }
    }
}
