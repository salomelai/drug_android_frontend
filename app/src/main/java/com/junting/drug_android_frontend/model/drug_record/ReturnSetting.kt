package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class ReturnSetting(
    var date: String,
    var left: Int,
    var repeat: Int,
    var status: Boolean
): Serializable