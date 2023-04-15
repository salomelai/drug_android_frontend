package com.junting.drug_android_frontend.model.drugbag_info

import java.io.Serializable

data class Drug(
    val appearance: String,
    val id: Int,
    val indications: String,
    val name: String,
    val sideEffect: String
): Serializable