package com.junting.drug_android_frontend.ui.todayReminder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import com.junting.drug_android_frontend.services.ITodayReminderService
import kotlinx.coroutines.launch

class TodayReminderViewModel : ViewModel() {

    var records = MutableLiveData<List<TodayReminder>>()

    fun fetchRecords() {
        viewModelScope.launch {
            val todayReminderService = ITodayReminderService.getInstance()
            try {
                records.value = todayReminderService.getTodayReminders()
            } catch (e: Exception) {
                Log.d("TodayReminderViewModel", "fetch records failed")
                Log.e("TodayReminderViewModel", e.toString())
            }
        }
    }
}