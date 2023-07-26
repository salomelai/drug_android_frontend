package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.Hospital
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IHospitalService {
    @GET("hospitals")
    suspend fun getHospitals(): List<Hospital>


    companion object {
        var hospitalService: IHospitalService? = null
        fun getInstance(): IHospitalService {
            if (hospitalService == null) {
                hospitalService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL_HEROKU)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(IHospitalService::class.java)
            }
            return hospitalService!!
        }
    }
}