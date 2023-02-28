package com.junting.drug_android_frontend.model

data class Drug(
    val appearance: String,
    val dosage: Int,
    val drug_name: String,
    val end_date: String,
    val hospital_department: String,
    val id: String,
    val indications: String,
    val interacting_drugs: List<InteractingDrug>,
    val side_effect: String,
    val start_date: String,
    val stock: Int,
    val time_slot: List<String>
)