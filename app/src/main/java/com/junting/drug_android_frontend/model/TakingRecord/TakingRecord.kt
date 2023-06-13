package com.junting.drug_android_frontend.model.TakingRecord

import java.io.Serializable

data class TakingRecord(
    var date: String,
    var dosage: Int,
    var drug: Drug,
    var id: Int,
    var position: Int,
    var status: Int,
    var timeSlot: String
): Serializable