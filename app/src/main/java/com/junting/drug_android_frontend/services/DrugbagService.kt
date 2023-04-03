package com.junting.drug_android_frontend.services

import android.util.Log
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DrugbagService(service: IDrugbagService) {

    private val service: IDrugbagService

    init {
        this.service = service
    }

    suspend fun getDrugbagInfo(): DrugbagInformation {
        try {
            // get token
            return service.getDrugbagInfo()
        } catch (e: Exception) {
            // handle 4xx 5xx error
            Log.e("DrugbagService", e.toString())
            throw java.lang.IllegalArgumentException("Failed to get drugbag info");
        }
    }

    companion object {
        var drugRecordService: DrugbagService? = null
        fun getInstance(): DrugbagService {
            if (drugRecordService == null) {
                drugRecordService = DrugbagService(IDrugbagService.getInstance())
            }
            return drugRecordService!!
        }
    }
}