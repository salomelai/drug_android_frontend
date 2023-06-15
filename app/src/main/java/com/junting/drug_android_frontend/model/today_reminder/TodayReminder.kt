package com.junting.drug_android_frontend.model.today_reminder

data class TodayReminder(
    val dosage: Int,
    val drug: Drug,
    val id: Int,
    val timeSlot: String
)