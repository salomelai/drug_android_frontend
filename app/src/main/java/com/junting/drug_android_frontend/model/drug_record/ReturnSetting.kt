package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class ReturnSetting(
    var date: String = "",
    var left: Int = 0,
    var repeat: Int = 0,
    var status: Boolean = false
): Serializable