package com.junting.drug_android_frontend.ui.takeRecords

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.TakeRecord.DateRecord
import com.junting.drug_android_frontend.model.TakeRecord.Medication
import com.junting.drug_android_frontend.model.TakeRecord.TakeRecord
import com.junting.drug_android_frontend.services.ITakeRecordService
import kotlinx.coroutines.launch

class TakeRecordsViewModel : ViewModel(){
    private val _takeRecords = MutableLiveData<List<TakeRecord>>()
    val takeRecords: LiveData<List<TakeRecord>> = _takeRecords

    private val _medications = MutableLiveData<List<Medication>>()
    val medications: LiveData<List<Medication>> = _medications


    fun fetchRecords() {
        viewModelScope.launch {
            val takeRecordService = ITakeRecordService.getInstance()
            try {
                val fetchedTakeRecords = takeRecordService.getTakeRecords()
                _takeRecords.value = fetchedTakeRecords
                groupByDrugAndDate(fetchedTakeRecords)
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
            val dateRecords = group.value.groupBy { it.date }
                .map { DateRecord(it.key, it.value) }

            val medication = Medication(drugName, dateRecords)
            medications.add(medication)
        }

        _medications.value = medications
    }
}