package com.junting.drug_android_frontend

import DialogUtils
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.junting.drug_android_frontend.databinding.ActionBarTakeRecordTodayReminderBinding
import com.junting.drug_android_frontend.databinding.ActivityTodayReminderBinding
import com.junting.drug_android_frontend.databinding.BottomSheetLaterBinding
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding
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

        todayReminderId = intent.getSerializableExtra("todayReminderId") as Int?
        if (todayReminderId != null) {
            this.todayReminderId = todayReminderId
            initViewModel()
        }
        // 隱藏所有藥物位置
        positions.forEach { i -> viewManager.hideCell(i,positions) }
        initActualTime()
        initButton()
        initClickableTextView()
    }
    private fun initViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchTodayReminderById(todayReminderId!!)
        viewModel.todayReminder.observe(this, Observer {
            Log.d("Observe todayReminder", "record: ${it.toString()}")
            bindingActionBarTakeRecordTodayReminder.tvDrugName.text = it.drug.name
            bindingActionBarTakeRecordTodayReminder.tvStock.text = it.stock.toString()+" 顆"
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
            "劑量",
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
                .setTitleText("選擇服藥時間")
                .setPositiveButtonText("確定")
                .setNegativeButtonText("取消")
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
                finish()
            }
        }

        binding.tvSkip.setOnClickListener {
            bottomSheetDialog.dismiss()
            finish()
        }

        bottomSheetDialog.show()
    }
    private fun showConfirmDialog() {

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("已服用藥物")
            .setView(bindingPillBox.root)
            .setPositiveButton("確定") { dialog, which ->
                finish()
            }
            .create()

        dialog.show()
    }

    private fun addMinutesToActualTime(minutes: Int) {

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = format.parse(viewModel.actualTakingTime.get()!!)!!
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)

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