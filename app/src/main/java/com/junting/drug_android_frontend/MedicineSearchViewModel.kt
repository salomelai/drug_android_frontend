package com.junting.drug_android_frontend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.Medicine
import com.junting.drug_android_frontend.services.IMedicineService
import kotlinx.coroutines.launch

class MedicineSearchViewModel : ViewModel() {

    val medicines = MutableLiveData<List<Medicine>>()


    fun fetchRecords() {
        viewModelScope.launch {
            val medicineService = IMedicineService.getInstance()
            try {
                medicines.value = medicineService.getMedicines()
            } catch (e: Exception) {
                Log.d("MedicineSearchViewModel", e.toString())
                Log.e("MedicineSearchViewModel", e.stackTraceToString())
            }
        }
    }
}