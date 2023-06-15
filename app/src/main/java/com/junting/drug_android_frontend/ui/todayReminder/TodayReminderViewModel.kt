package com.junting.drug_android_frontend.ui.todayReminder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.TakeRecord.TakeRecord
import com.junting.drug_android_frontend.services.ITakeRecordService
import kotlinx.coroutines.launch

class TodayReminderViewModel : ViewModel() {

    var records = MutableLiveData<List<TakeRecord>>()

    fun fetchRecords() {
        viewModelScope.launch {
            val takeRecordService = ITakeRecordService.getInstance()
            try {
                records.value = takeRecordService.getTakeRecordsByDate("2023/01/02",0)
            } catch (e: Exception) {
                Log.d("TodayReminderViewModel", "fetch dateTakeRecordsRecords failed")
                Log.e("TodayReminderViewModel", e.toString())
            }
        }
    }
}