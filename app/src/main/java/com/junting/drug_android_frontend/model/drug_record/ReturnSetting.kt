package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class ReturnSetting(
    val date: String,
    val left: Int,
    val repeat: Int,
    val status: Boolean
): Serializable