package com.junting.drug_android_frontend.model.take_record

data class DateTimeSlotRecord(
    var date: String,
    var timeSlotRecords: List<TimeSlotRecord>
)