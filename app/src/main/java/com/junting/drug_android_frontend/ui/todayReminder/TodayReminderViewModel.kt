package com.junting.drug_android_frontend.ui.todayReminder

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import com.junting.drug_android_frontend.services.ITodayReminderService
import kotlinx.coroutines.launch

class TodayReminderViewModel : ViewModel() {

    var todayReminders = MutableLiveData<List<TodayReminder>>()
    var todayReminder = MutableLiveData<TodayReminder>()
    val actualTakingTime  = ObservableField<String>()

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
    fun fetchTodayReminderById(id:Int){
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
}