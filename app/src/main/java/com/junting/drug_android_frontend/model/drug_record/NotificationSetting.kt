package com.junting.drug_android_frontend.model.drug_record

data class NotificationSetting(
    val remind: Int,
    val repeat: Int,
    val startDate: String,
    val status: Boolean
)