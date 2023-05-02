package com.junting.drug_android_frontend.model.drugbag_info

import java.io.Serializable

data class DrugbagInformation(
    var dosage: Int,
    var drug: Drug,
    var frequency: Int,
    var hospitalDepartment: String,
    var hospitalName: String,
    var id: Int,
    var onDemand: Boolean,
    var stock: Int,
    var timings: Set<Int>
): Serializable