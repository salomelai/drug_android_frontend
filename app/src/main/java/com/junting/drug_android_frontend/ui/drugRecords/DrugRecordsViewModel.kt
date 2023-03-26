package com.junting.drug_android_frontend.ui.drugRecords

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.services.DrugRecordService
import kotlinx.coroutines.launch

class DrugRecordsViewModel : ViewModel() {

    var records = MutableLiveData<List<DrugRecord>>()

    fun fetchRecords() {
        viewModelScope.launch {
            val drugRecordService = DrugRecordService.getInstance()
            try {
                records.value = drugRecordService.getDrugs()
            } catch (e: Exception) {
                Log.d("DrugsViewModel", "fetch records failed")
                Log.e("DrugsViewModel", e.toString())
            }
        }
    }
}