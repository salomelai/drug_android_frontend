package com.junting.drug_android_frontend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.drug_record.InteractingDrug
import com.junting.drug_android_frontend.services.IDrugInteractionService
import kotlinx.coroutines.launch

class DrugInteractionViewModel : ViewModel() {
    var drugInteractions = MutableLiveData<List<InteractingDrug>>()

    fun fetchDrugInteraction() {
        viewModelScope.launch {
            val service = IDrugInteractionService.getInstance()
            try {
                drugInteractions.value = service.getInteractingDrugs()
            } catch (e: Exception) {
                Log.d("DrugInteractionViewModel", "Error: ${e.message}")
            }
        }
    }
}