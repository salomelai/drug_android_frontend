package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class NotificationSetting(
    var remind: Int,
    var repeat: Int,
    var startDate: String,
    var status: Boolean
): Serializable