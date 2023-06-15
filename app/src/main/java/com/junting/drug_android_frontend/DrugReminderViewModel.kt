package com.junting.drug_android_frontend

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.junting.drug_android_frontend.model.take_record.TakeRecord

class DrugReminderViewModel : ViewModel() {
    val takeRecord = MutableLiveData<TakeRecord>()
    val actualTakingTime  = ObservableField<String>()

    fun setDosage(dosage: Int) {
        val info: TakeRecord = takeRecord.value!!
        info.dosage = dosage
        triggerUpdate(info)
    }
    fun setTimeSlot(timeSlot: String) {
        val info: TakeRecord = takeRecord.value!!
        info.timeSlot = timeSlot
        triggerUpdate(info)
    }
    fun setActualTakingTime(actualTakingTime: String) {
        this.actualTakingTime.set(actualTakingTime)
    }

    private fun triggerUpdate(newTakeRecord: TakeRecord) {
        takeRecord.value = newTakeRecord
    }

}