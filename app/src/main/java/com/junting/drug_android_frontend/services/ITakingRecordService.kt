package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.TakingRecord.TakingRecord
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ITakingRecordService {
    @GET("takingRecords/")
    suspend fun getTakingRecords(): List<TakingRecord>

    @GET("takingRecords")
    suspend fun getTakingRecordsByDate(
        @Query("date") date: String,
        @Query("status") status: Int
    ): List<TakingRecord>


    companion object {
        var takingRecordService: ITakingRecordService? = null
        fun getInstance(): ITakingRecordService {
            if (takingRecordService == null) {
                takingRecordService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ITakingRecordService::class.java)
            }
            return takingRecordService!!
        }
    }
}