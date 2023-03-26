package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IDrugbagService {
    @GET("drugBagInformation/10/")
    suspend fun getDrugbagInfo(): DrugbagInformation

    companion object {
        var drugRecordService: IDrugbagService? = null
        fun getInstance(): IDrugbagService {
            if (drugRecordService == null) {
                drugRecordService = Retrofit.Builder()
                    .baseUrl("https://my-json-server.typicode.com/JunTingLin/drug-json-api-server/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(IDrugbagService::class.java)
            }
            return drugRecordService!!
        }
    }
}