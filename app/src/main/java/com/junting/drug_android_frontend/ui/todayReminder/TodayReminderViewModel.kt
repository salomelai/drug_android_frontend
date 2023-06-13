package com.junting.drug_android_frontend.ui.todayReminder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.TakingRecord.TakingRecord
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import com.junting.drug_android_frontend.services.ITakingRecordService
import com.junting.drug_android_frontend.services.ITodayReminderService
import kotlinx.coroutines.launch

class TodayReminderViewModel : ViewModel() {

    var records = MutableLiveData<List<TakingRecord>>()

    fun fetchRecords() {
        viewModelScope.launch {
            val takingRecordService = ITakingRecordService.getInstance()
            try {
                records.value = takingRecordService.getTakingRecordsByDate("2023/01/02",0)
            } catch (e: Exception) {
                Log.d("TodayReminderViewModel", "fetch records failed")
                Log.e("TodayReminderViewModel", e.toString())
            }
        }
    }
}