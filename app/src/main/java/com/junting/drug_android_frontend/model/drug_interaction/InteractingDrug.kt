package com.junting.drug_android_frontend.model.drug_interaction

data class InteractingDrug(
    val cause: String,
    val degree: String,
    val name: String,
    val timeSlots: List<String>
)