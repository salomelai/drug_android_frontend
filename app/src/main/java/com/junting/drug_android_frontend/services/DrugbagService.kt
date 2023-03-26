package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DrugbagService {
    @GET("drugRecords/")
    suspend fun getDrugs(): List<DrugRecord>

    companion object {
        var drugRecordService: DrugbagService? = null
        fun getInstance(): DrugbagService {
            if (drugRecordService == null) {
                drugRecordService = Retrofit.Builder()
                    .baseUrl("https://my-json-server.typicode.com/JunTingLin/drug-json-api-server/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(DrugbagService::class.java)
            }
            return drugRecordService!!
        }
    }
}