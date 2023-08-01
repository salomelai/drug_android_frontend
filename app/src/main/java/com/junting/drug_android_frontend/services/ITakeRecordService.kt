package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.ResponseMessage
import com.junting.drug_android_frontend.model.take_record.TakeRecord
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ITakeRecordService {
    @GET("takeRecords")
    suspend fun getTakeRecords(): List<TakeRecord>

    // 獲取指定 ID 的 TakeRecord
    @GET("takeRecords/{id}")
    suspend fun getTakeRecordById(@Path("id") id: Int): TakeRecord

    // 新增一筆 TakeRecord(會根據status而有不同狀況)
    @POST("takeRecords")
    suspend fun processTakeRecord(@Body takeRecord: TakeRecord): Response<ResponseMessage>

    // 修改指定 ID 的 TakeRecord
    @PUT("takeRecords/{id}")
    suspend fun updateTakeRecordById(@Path("id") id: Int, @Body takeRecord: TakeRecord): Response<TakeRecord>

    // 刪除指定 ID 的 TakeRecord
    @DELETE("takeRecords/{id}")
    suspend fun deleteTakeRecordById(@Path("id") id: Int): Response<TakeRecord>


    companion object {
        var takeRecordService: ITakeRecordService? = null
        fun getInstance(): ITakeRecordService {
            if (takeRecordService == null) {
                takeRecordService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL_PYTHONANYWHERE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ITakeRecordService::class.java)
            }
            return takeRecordService!!
        }
    }
}