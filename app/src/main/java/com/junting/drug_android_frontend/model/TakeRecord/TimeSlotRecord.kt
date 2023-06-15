package com.junting.drug_android_frontend.model.TakeRecord

data class TimeSlotRecord(
    val hour: String,
    val takeRecords: List<TakeRecord>
)