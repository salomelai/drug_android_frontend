package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IDrugbagService {
    @GET("drugBagInformation/10/")
    suspend fun getDrugbagInfo(): DrugbagInformation

    @POST("drugBagInformation/")
    suspend fun postDrugInfo(@Body params: DrugbagInformation)

    companion object {
        var drugRecordService: IDrugbagService? = null
        fun getInstance(): IDrugbagService {
            if (drugRecordService == null) {
                drugRecordService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL_HEROKU)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(IDrugbagService::class.java)
            }
            return drugRecordService!!
        }
    }
}