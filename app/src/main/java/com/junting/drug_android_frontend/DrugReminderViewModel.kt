package com.junting.drug_android_frontend

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.junting.drug_android_frontend.model.TakingRecord.TakingRecord
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder

class DrugReminderViewModel : ViewModel() {
    val takingRecord = MutableLiveData<TakingRecord>()
    val actualTakingTime  = ObservableField<String>()

    fun setDosage(dosage: Int) {
        val info: TakingRecord = takingRecord.value!!
        info.dosage = dosage
        triggerUpdate(info)
    }
    fun setTimeSlot(timeSlot: String) {
        val info: TakingRecord = takingRecord.value!!
        info.timeSlot = timeSlot
        triggerUpdate(info)
    }
    fun setActualTakingTime(actualTakingTime: String) {
        this.actualTakingTime.set(actualTakingTime)
    }

    private fun triggerUpdate(newTakingRecord: TakingRecord) {
        takingRecord.value = newTakingRecord
    }

}