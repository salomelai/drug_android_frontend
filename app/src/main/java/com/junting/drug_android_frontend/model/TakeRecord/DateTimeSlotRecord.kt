package com.junting.drug_android_frontend.model.TakeRecord

data class DateTimeSlotRecord(
    var date: String,
    var timeSlotRecords: List<TimeSlotRecord>
)