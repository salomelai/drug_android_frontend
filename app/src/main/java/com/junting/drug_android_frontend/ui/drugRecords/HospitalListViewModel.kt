package com.junting.drug_android_frontend.ui.drugRecords

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.Hospital
import com.junting.drug_android_frontend.services.IHospitalService
import kotlinx.coroutines.launch

class HospitalListViewModel : ViewModel() {

    val hospitals = MutableLiveData<List<Hospital>>()


    fun fetchRecords() {
        viewModelScope.launch {
            val hospitalService = IHospitalService.getInstance()
            try {
                hospitals.value = hospitalService.getHospitals()
            } catch (e: Exception) {
                Log.d("DrugsViewModel", e.toString())
                Log.e("DrugsViewModel", e.stackTraceToString())
            }
        }
    }
}