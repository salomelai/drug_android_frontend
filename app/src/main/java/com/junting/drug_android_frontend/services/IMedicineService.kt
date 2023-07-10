package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.model.Medicine
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IMedicineService {
    @GET("country-by-name.json")
    suspend fun getMedicines(): List<Medicine>


    companion object {
        var medicineService: IMedicineService? = null
        fun getInstance(): IMedicineService {
            if (medicineService == null) {
                medicineService = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/samayo/country-json/master/src/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(IMedicineService::class.java)
            }
            return medicineService!!
        }
    }
}