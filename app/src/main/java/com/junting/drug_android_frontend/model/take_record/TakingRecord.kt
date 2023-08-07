package com.junting.drug_android_frontend.model.take_record

import java.io.Serializable

data class TakeRecord(
    var drugRecordId: Int = 0,
    var todayReminderId: Int = 0,
    var date: String = "",
    var dosage: Int = 0,
    var drug: Drug = Drug(),
    var id: Int = 0,
    var position: Int = 0,
    var status: Int = 0,
    var timeSlot: String = "",
    var batchTime: Int = 999999
): Serializable