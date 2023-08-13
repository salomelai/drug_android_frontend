package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class ReturnSetting(
    var date: String = "1999/01/01",
    var left: Int = 0,
    var repeat: Int = 0,
    var status: Boolean = false
): Serializable