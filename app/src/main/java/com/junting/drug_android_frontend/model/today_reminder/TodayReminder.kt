package com.junting.drug_android_frontend.model.today_reminder

import java.io.Serializable

data class TodayReminder(
    val dosage: Int,
    val drug: Drug,
    val id: Int,
    var timeSlot: String
): Serializable