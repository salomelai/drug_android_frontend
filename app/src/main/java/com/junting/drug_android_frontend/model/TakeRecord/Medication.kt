package com.junting.drug_android_frontend.model.TakeRecord

data class Medication(
    var name: String,
    var dateRecords: List<DateRecord>
)
