package com.junting.drug_android_frontend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import com.junting.drug_android_frontend.services.IDrugbagService
import kotlinx.coroutines.launch

class DrugbagInfoViewModel : ViewModel() {
    val drugbagInfo = MutableLiveData<DrugbagInformation>()
    fun fetchDrugbagInfo() {
        viewModelScope.launch {
            val service = IDrugbagService.getInstance()
            try {
                drugbagInfo.value = service.getDrugbagInfo()
            }catch (e: Exception) {
                Log.e("DrugbagInfoViewModel", "Error: ${e.message}")
            }
        }
    }
    fun sendDrugbagInfo(drugbagInfo: DrugbagInformation) {
        viewModelScope.launch {
            val service = IDrugbagService.getInstance()
            try {
                service.postDrugInfo(drugbagInfo)
            }catch (e: Exception) {
                Log.e("DrugbagInfoViewModel", "Error: ${e.message}")
            }
        }
    }
}