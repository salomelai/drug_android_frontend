package com.junting.drug_android_frontend.ui.todayReminder

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.ResponseMessage
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.take_record.TakeRecord
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import com.junting.drug_android_frontend.services.IDrugRecordService
import com.junting.drug_android_frontend.services.ITakeRecordService
import com.junting.drug_android_frontend.services.ITodayReminderService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TodayReminderViewModel : ViewModel() {

    var todayReminders = MutableLiveData<List<TodayReminder>>()
    var todayReminder = MutableLiveData<TodayReminder>()
    var drugRecors = MutableLiveData<List<DrugRecord>>()
    val actualTakingTime = ObservableField<String>()

    fun setDosage(dosage: Int) {
        val info: TodayReminder = todayReminder.value!!
        info.dosage = dosage
        triggerUpdate(info)
    }

    fun setTimeSlot(timeSlot: String) {
        val info: TodayReminder = todayReminder.value!!
        info.timeSlot = timeSlot
        triggerUpdate(info)
    }

    fun setActualTakingTime(actualTakingTime: String) {
        this.actualTakingTime.set(actualTakingTime)
        Log.d("TodayReminderViewModel", "set actualTakingTime=${actualTakingTime}")
    }

    private fun triggerUpdate(newTodayReminder: TodayReminder) {
        todayReminder.value = newTodayReminder
    }

    fun fetchTodayReminders() {
        viewModelScope.launch {
            val todayReminderService = ITodayReminderService.getInstance()
            try {
                todayReminders.value = todayReminderService.getTodayReminders()
            } catch (e: Exception) {
                Log.d("TodayReminderViewModel", "fetch todayReminders failed")
                Log.e("TodayReminderViewModel", e.toString())
            }
        }
    }

    fun fetchTodayReminderById(id: Int) {
        viewModelScope.launch {
            val todayReminderService = ITodayReminderService.getInstance()
            try {
                todayReminder.value = todayReminderService.getTodayReminderById(id)
            } catch (e: Exception) {
                Log.d("TodayReminderViewModel", "fetch todayReminder id=${id} failed")
                Log.e("TodayReminderViewModel", e.toString())
            }
        }
    }

    fun fetchDrugRecords() {
        viewModelScope.launch {
            val drugRecordService = IDrugRecordService.getInstance()
            try {
                drugRecors.value = drugRecordService.getDrugs()
            } catch (e: Exception) {
                Log.d("TodayReminderViewModel", "fetch drugRecords failed")
                Log.e("TodayReminderViewModel", e.toString())
            }
        }
    }

    fun processTakeRecord(takeRecord: TakeRecord): Deferred<ResponseMessage?> {
        return viewModelScope.async {
            val takeRecordService = ITakeRecordService.getInstance()
            try {
                val response = takeRecordService.processTakeRecord(takeRecord)
                if (response.isSuccessful) {
                    val message = response.body()
                    if (message != null) {
                        Log.d("TodayReminderViewModel", "process takeRecord success")
                        message
                    } else {
                        // 处理ResponseMessage为null的情况
                        throw IllegalStateException("Response body is null")
                    }
                } else {
                    Log.d("TodayReminderViewModel", "process takeRecord failed")
                    null
                }
            } catch (e: Exception) {
                // 处理异常
                Log.d("TodayReminderViewModel", "process TakeRecord failed")
                Log.e("TodayReminderViewModel", e.toString())
                null
            }
        }
    }
}
