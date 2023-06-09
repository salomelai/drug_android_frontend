package com.junting.drug_android_frontend

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder

class DrugReminderViewModel : ViewModel() {
    val todayReminder = MutableLiveData<TodayReminder>()
    var actualTakeingTime  = ObservableField<String>()

}