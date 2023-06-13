package com.junting.drug_android_frontend.model.TakeRecord

import java.io.Serializable

data class TakeRecord(
    var date: String,
    var dosage: Int,
    var drug: Drug,
    var id: Int,
    var position: Int,
    var status: Int,
    var timeSlot: String
): Serializable