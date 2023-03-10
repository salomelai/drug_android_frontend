package com.junting.drug_android_frontend.ui.todayReminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodayReminderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is " +
                "today Reminder Fragment"
    }
    val text: LiveData<String> = _text
}