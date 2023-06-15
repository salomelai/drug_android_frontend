package com.junting.drug_android_frontend.model.take_record

data class TimeSlotRecord(
    val hour: String,
    val takeRecords: List<TakeRecord>
)