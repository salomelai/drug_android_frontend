package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class NotificationSetting(
    val remind: Int,
    val repeat: Int,
    val startDate: String,
    val status: Boolean
): Serializable