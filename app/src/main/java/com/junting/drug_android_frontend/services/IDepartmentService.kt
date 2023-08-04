package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.Department
import com.junting.drug_android_frontend.model.Hospital
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IDepartmentService {
    @GET("hospitalDepartments")
    suspend fun getDepartments(): List<Department>


    companion object {
        var departmentService: IDepartmentService? = null
        fun getInstance(): IDepartmentService {
            if (departmentService == null) {
                departmentService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL_PYTHONANYWHERE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(IDepartmentService::class.java)
            }
            return departmentService!!
        }
    }
}