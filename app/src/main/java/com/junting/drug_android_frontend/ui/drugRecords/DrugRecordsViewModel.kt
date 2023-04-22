package com.junting.drug_android_frontend.ui.drugRecords

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import com.junting.drug_android_frontend.services.IDrugRecordService
import kotlinx.coroutines.launch

class DrugRecordsViewModel : ViewModel() {

    val records = MutableLiveData<List<DrugRecord>>()
    val record = MutableLiveData<DrugRecord>()

    fun setDrugName(name: String) {
        val info: DrugRecord = record.value!!
        info.drug.name = name
    }

    fun fetchRecords() {
        viewModelScope.launch {
            val drugRecordService = IDrugRecordService.getInstance()
            try {
                records.value = drugRecordService.getDrugs()
            } catch (e: Exception) {
                Log.d("DrugsViewModel", "fetch records failed")
                Log.e("DrugsViewModel", e.toString())
            }
        }
    }
    fun fetchRecord(id: Int) {
        viewModelScope.launch {
            val drugRecordService = IDrugRecordService.getInstance()
            try {
                record.value = drugRecordService.getDrugById(id)
            } catch (e: Exception) {
                Log.d("EditDrugRecordViewModel", "fetch record id=${id} failed")
                Log.e("EditDrugRecordViewModel", e.toString())
            }
        }
    }
}