package com.junting.drug_android_frontend.services

import com.junting.drug_android_frontend.constants.DataApiConstants
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ITodayReminderService {
    @GET("todayReminders/")
    suspend fun getTodayReminders(): List<TodayReminder>

    // 獲取指定 ID 的 TodayReminder
    @GET("todayReminders/{id}")
    suspend fun getTodayReminderById(@Path("id") id: Int): TodayReminder


    companion object {
        var todayReminderService: ITodayReminderService? = null
        fun getInstance(): ITodayReminderService {
            if (todayReminderService == null) {
                todayReminderService = Retrofit.Builder()
                    .baseUrl(DataApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ITodayReminderService::class.java)
            }
            return todayReminderService!!
        }
    }
}