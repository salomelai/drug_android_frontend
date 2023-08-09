package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.drug_record.InteractingDrug
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface IDrugInteractionService {
//    @GET("interactionsWithDrugsAlreadyTaking0/")
//    suspend fun getInteractingDrugs(): List<InteractingDrug>

    @GET("interactionsWithDrugsAlreadyTaking/drugName/{drugName}")
    suspend fun getInteractingDrugs(@Path("drugName") drugName: String): List<InteractingDrug>


    companion object {
        var drugInteractionService: IDrugInteractionService? = null
        fun getInstance(): IDrugInteractionService {
            if (drugInteractionService == null) {
                drugInteractionService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL_PYTHONANYWHERE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(IDrugInteractionService::class.java)
            }
            return drugInteractionService!!
        }
    }
}