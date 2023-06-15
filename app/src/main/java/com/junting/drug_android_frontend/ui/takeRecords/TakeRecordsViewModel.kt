package com.junting.drug_android_frontend.ui.takeRecords

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.TakeRecord.DateTakeRecordsRecord
import com.junting.drug_android_frontend.model.TakeRecord.DateTimeSlotRecord
import com.junting.drug_android_frontend.model.TakeRecord.Medication
import com.junting.drug_android_frontend.model.TakeRecord.TakeRecord
import com.junting.drug_android_frontend.model.TakeRecord.TimeSlotRecord
import com.junting.drug_android_frontend.services.ITakeRecordService
import kotlinx.coroutines.launch

class TakeRecordsViewModel : ViewModel(){
    private val _takeRecords = MutableLiveData<List<TakeRecord>>()
    val takeRecords: LiveData<List<TakeRecord>> = _takeRecords

    private val _medications = MutableLiveData<List<Medication>>()
    val medications: LiveData<List<Medication>> = _medications

    private val _dateTimeSlotRecord = MutableLiveData<List<DateTimeSlotRecord>>()
    val dateTimeSlotRecords: LiveData<List<DateTimeSlotRecord>> = _dateTimeSlotRecord


    fun fetchRecords() {
        viewModelScope.launch {
            val takeRecordService = ITakeRecordService.getInstance()
            try {
                val fetchedTakeRecords = takeRecordService.getTakeRecords()
                _takeRecords.value = fetchedTakeRecords
                groupByDrugAndDate(fetchedTakeRecords)
                groupByDateAndTime(fetchedTakeRecords)
            } catch (e: Exception) {
                Log.d("TakeRecordsViewModel", "fetch takeRecords failed")
                Log.e("TakeRecordsViewModel", e.toString())
            }
        }
    }
    private fun groupByDrugAndDate(takeRecords: List<TakeRecord>) {
        val groupedByDrugAndDate = takeRecords.groupBy { it.drug.name }
        val medications = mutableListOf<Medication>()

        for (group in groupedByDrugAndDate) {
            val drugName = group.key
            val dateTakeRecordsRecords = group.value.groupBy { it.date }
                .map { DateTakeRecordsRecord(it.key, it.value) }

            val medication = Medication(drugName, dateTakeRecordsRecords)
            medications.add(medication)
        }

        _medications.value = medications
    }
    private fun groupByDateAndTime(takeRecords: List<TakeRecord>) {
        val groupedByDate = takeRecords.groupBy { it.date }

        val dateTimeSlotRecords = mutableListOf<DateTimeSlotRecord>()
        for (date in groupedByDate.keys) {
            val recordsForDate = groupedByDate[date] ?: emptyList()

            // Group by time slot hour
            val groupedByTimeSlotHour = recordsForDate.groupBy { it.timeSlot.substringBefore(":") }

            val timeSlotRecords = mutableListOf<TimeSlotRecord>()
            for (hour in groupedByTimeSlotHour.keys) {
                val takeRecordsForHour = groupedByTimeSlotHour[hour] ?: emptyList()
                val timeSlotRecord = TimeSlotRecord(hour, takeRecordsForHour)
                timeSlotRecords.add(timeSlotRecord)
            }

            val dateTimeSlotRecord = DateTimeSlotRecord(date, timeSlotRecords)
            dateTimeSlotRecords.add(dateTimeSlotRecord)
        }

        _dateTimeSlotRecord.value = dateTimeSlotRecords
    }
}