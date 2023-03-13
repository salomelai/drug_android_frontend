package com.junting.drug_android_frontend.model

data class Record(
    val dosage: Int,
    val drug: Drug,
    val endDate: String,
    val id: Int,
    val interactingDrugs: List<InteractingDrug>,
    val startDate: String,
    val stock: Int,
    val timeSlot: List<String>
)