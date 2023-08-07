package com.junting.drug_android_frontend

import DialogUtils
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.junting.drug_android_frontend.databinding.ActionBarTakeRecordTodayReminderBinding
import com.junting.drug_android_frontend.databinding.ActivityTodayReminderBinding
import com.junting.drug_android_frontend.databinding.BottomSheetLaterBinding
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding
import com.junting.drug_android_frontend.model.take_record.TakeRecord
import com.junting.drug_android_frontend.ui.todayReminder.TodayReminderViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodayReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodayReminderBinding
    private lateinit var bindingPillBox: FragmentPillBoxManagementBinding
    private lateinit var bindingActionBarTakeRecordTodayReminder: ActionBarTakeRecordTodayReminderBinding
    var todayReminderId: Int? = null
    private var viewModel: TodayReminderViewModel = TodayReminderViewModel()
    private lateinit var viewManager: PillBoxViewManager
    private val positions = (1..9).toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayReminderBinding.inflate(layoutInflater)
        bindingPillBox = FragmentPillBoxManagementBinding.inflate(layoutInflater)
        bindingActionBarTakeRecordTodayReminder = ActionBarTakeRecordTodayReminderBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        viewManager = PillBoxViewManager(bindingPillBox,this) // Initialize the view manager

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)
//        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)  //完全自定義
        supportActionBar?.setTitle("")
        supportActionBar?.setCustomView(bindingActionBarTakeRecordTodayReminder.root)

        todayReminderId = intent.getSerializableExtra("takeRecordId") as Int?
        if (todayReminderId != null) {
            this.todayReminderId = todayReminderId
            initViewModel()
        }
        // 隱藏所有藥物位置
        positions.forEach { i -> viewManager.hideCell(i,positions) }
        initActualTime()
        initButton()
        initClickableTextView()

        val inflater = LayoutInflater.from(this)
        val instructionLayout = inflater.inflate(R.layout.instruction_background, bindingPillBox.llInstruction, false)
        bindingPillBox.llInstruction.addView(instructionLayout)
    }
    private fun initViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchTodayReminderById(todayReminderId!!)
        viewModel.todayReminder.observe(this, Observer {
            Log.d("Observe todayReminder", "record: ${it.toString()}")
            bindingActionBarTakeRecordTodayReminder.tvDrugName.text = it.drug.name
            bindingActionBarTakeRecordTodayReminder.tvStock.text = resources.getString(R.string.stock)+":"+it.stock.toString()+" "+resources.getString(R.string.unit)
            viewManager.setCellColor(it.position)
            binding.progressBar.visibility = View.GONE
        })
        viewModel.fetchDrugRecords()
        viewModel.drugRecors.observe(this, Observer {

            // 遍历记录并更新 UI
            for (record in it) {
                when (record.position) {
                    1 -> viewManager.showCell(1, record,false)
                    2 -> viewManager.showCell(2, record,false)
                    3 -> viewManager.showCell(3, record,false)
                    4 -> viewManager.showCell(4, record,false)
                    5 -> viewManager.showCell(5, record,false)
                    6 -> viewManager.showCell(6, record,false)
                    7 -> viewManager.showCell(7, record,false)
                    8 -> viewManager.showCell(8, record,false)
                    9 -> viewManager.showCell(9, record,false)
                }
            }
            positions.forEach { i -> viewManager.closeProgressBar(i) }
        })
    }

    private fun initClickableTextView() {
        DialogUtils.initTextViewEditDialog(
            this,
            binding.llDosage,
            binding.tvDosage,
            resources.getString(R.string.dosage),
            true
        ) { text ->
            viewModel.setDosage(text.toInt())
        }
        binding.llTimeSlot.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val timePicker = MaterialTimePicker.Builder()
                .setHour(hour)
                .setMinute(minute)
                .setTitleText(resources.getString(R.string.take_record_select_time))
                .setPositiveButtonText(resources.getString(R.string.confirm))
                .setNegativeButtonText(resources.getString(R.string.cancel))
                .build()


            timePicker.addOnPositiveButtonClickListener {
                val selectedHour = timePicker.hour
                val selectedMinute = timePicker.minute

                viewModel.setActualTakingTime(
                    String.format(
                        "%02d:%02d",
                        selectedHour,
                        selectedMinute
                    )
                )
            }

            timePicker.show(supportFragmentManager, "timePicker")
        }
    }


    private fun initActualTime() {
        val currentTime = Calendar.getInstance()
        viewModel.actualTakingTime.set(
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                currentTime.time
            )
        )
    }

    private fun initButton() {
        binding.btnLater.setOnClickListener {
            showDelayBottomSheet()
        }
        binding.btnConfirm.setOnClickListener{
            showConfirmDialog()
        }
    }


    fun showDelayBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = BottomSheetLaterBinding.inflate(layoutInflater)
        val view = binding.root
        bottomSheetDialog.setContentView(view)


        val delayMinutes = listOf(10, 30, 45)

        for (minutes in delayMinutes) {
            val textView = when (minutes) {
                10 -> binding.tvDelay10Minutes
                30 -> binding.tvDelay30Minutes
                45 -> binding.tvDelay45Minutes
                else -> null
            }

            textView?.setOnClickListener {
                addMinutesToActualTime(minutes)
                bottomSheetDialog.dismiss()

                var takeRecord = TakeRecord(
                    todayReminderId = viewModel.todayReminder.value!!.id,
                    status = 3,
                    dosage = viewModel.todayReminder.value!!.dosage,
                    timeSlot = viewModel.actualTakingTime.get()!!
                )
                viewModel.processTakeRecord(takeRecord)

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("fragmentName", "TodayReminderFragment")
                startActivity(intent)
            }
        }

        binding.tvSkip.setOnClickListener {
            bottomSheetDialog.dismiss()

            var takeRecord = TakeRecord(
                todayReminderId = viewModel.todayReminder.value!!.id,
                status = 2
            )
            viewModel.processTakeRecord(takeRecord)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("fragmentName", "TodayReminderFragment")
            startActivity(intent)
        }

        bottomSheetDialog.show()
    }
    private fun showConfirmDialog() {
        val parentView = bindingPillBox.root.parent as? ViewGroup
        parentView?.removeView(bindingPillBox.root)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.taken_drug))
            .setView(bindingPillBox.root)
            .setPositiveButton(resources.getString(R.string.close_pillbox)) { dialog, which ->
                var takeRecord = TakeRecord(
                    todayReminderId = viewModel.todayReminder.value!!.id,
                    status = 1,
                    dosage = viewModel.todayReminder.value!!.dosage,
                    timeSlot = viewModel.actualTakingTime.get()!!
                )
                viewModel.processTakeRecord(takeRecord)

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("fragmentName", "TodayReminderFragment")
                startActivity(intent)
            }
            .create()

        dialog.show()
    }

    private fun addMinutesToActualTime(minutes: Int) {

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = format.parse(viewModel.actualTakingTime.get()!!)!!
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
            calendar.add(Calendar.MINUTE, minutes)
            val newTime = format.format(calendar.time)
            viewModel.setActualTakingTime(newTime)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}