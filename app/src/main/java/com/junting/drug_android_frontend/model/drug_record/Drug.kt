package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class Drug(
    val appearance: String,
    val id: Int,
    val indication: String,
    val name: String,
    val sideEffect: String
): Serializable