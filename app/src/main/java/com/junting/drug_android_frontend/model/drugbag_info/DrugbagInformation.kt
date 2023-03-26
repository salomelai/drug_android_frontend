package com.junting.drug_android_frontend.model.drugbag_info

data class DrugbagInformation(
    val dosage: Int,
    val drug: Drug,
    val frequency: Int,
    val hospitalDepartment: String,
    val hospitalName: String,
    val id: Int,
    val onDemand: Boolean,
    val stock: Int,
    val timings: List<Int>
)