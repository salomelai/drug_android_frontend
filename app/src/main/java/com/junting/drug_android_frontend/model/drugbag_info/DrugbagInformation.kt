package com.junting.drug_android_frontend.model.drugbag_info

import java.io.Serializable

data class DrugbagInformation(
    var dosage: Int = 0,
    var drug: Drug = Drug(),
    var frequency: Int = 0,
    var hospital: Hospital = Hospital(),
    var id: Int = 0,
    var onDemand: Boolean = false,
    var stock: Int = 0,
    var timings: Set<Int> = emptySet()
): Serializable