package com.junting.drug_android_frontend

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder

class DrugReminderViewModel : ViewModel() {
    val todayReminder = MutableLiveData<TodayReminder>()
    val actualTakeingTime  = ObservableField<String>()

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
    fun setActualTakeingTime(actualTakeingTime: String) {
        this.actualTakeingTime.set(actualTakeingTime)
    }

    private fun triggerUpdate(newTodayReminder: TodayReminder) {
        todayReminder.value = newTodayReminder
    }

}