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
import com.junting.drug_android_frontend.databinding.ActivityTakeRecordBinding
import com.junting.drug_android_frontend.databinding.ActivityTodayReminderBinding
import com.junting.drug_android_frontend.databinding.BottomSheetLaterBinding
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding
import com.junting.drug_android_frontend.ui.takeRecords.TakeRecordsViewModel
import com.junting.drug_android_frontend.ui.todayReminder.TodayReminderViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TakeRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakeRecordBinding
    var takeRecordId: Int? = null
    private var viewModel: TakeRecordsViewModel = TakeRecordsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakeRecordBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        takeRecordId = intent.getSerializableExtra("takeRecordId") as Int?
        if (takeRecordId != null) {
            this.takeRecordId = takeRecordId
            initViewModel()
        }

        initButton()
        initClickableTextView()
    }
    private fun initViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchTakeRecordById(takeRecordId!!)
        viewModel.takeRecord.observe(this, Observer {
            Log.d("Observe takeRecord", "record: ${it.toString()}")
            supportActionBar?.setTitle(it.drug.name)

            binding.progressBar.visibility = View.GONE
        })

    }

    private fun initClickableTextView() {
        DialogUtils.initTextViewEditDialog(this,binding.llDosage,binding.tvDosage, "劑量", true) {
                text -> viewModel.setDosage(text.toInt())
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

                viewModel.setTimeSlot(
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



    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
        binding.btnConfirm.setOnClickListener{
            onBackPressed()
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