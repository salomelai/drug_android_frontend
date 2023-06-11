package com.junting.drug_android_frontend.model.today_reminder

import java.io.Serializable

data class TodayReminder(
    var dosage: Int,
    var drug: Drug,
    var id: Int,
    var timeSlot: String,
    var position: Int
): Serializable