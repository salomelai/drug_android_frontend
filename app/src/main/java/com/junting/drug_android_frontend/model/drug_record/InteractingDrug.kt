package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class InteractingDrug(
    val cause: String = "",
    val id: Int = 0,
    val degree: String = "",
    val name: String = "",
    val timeSlots: List<String> = emptyList(),
    val duplicate: Boolean = false
): Serializable