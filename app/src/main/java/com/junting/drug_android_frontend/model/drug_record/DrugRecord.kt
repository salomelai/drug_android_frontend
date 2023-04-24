package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class DrugRecord(
    var dosage: Int,
    var drug: Drug,
    var frequency: Int,
    var hospitalDepartment: String,
    var hospitalName: String,
    var id: Int,
    var interactingDrugs: List<InteractingDrug>?,
    var notificationSetting: NotificationSetting,
    var onDemand: Boolean,
    var position: Int,
    var returnSetting: ReturnSetting,
    var stock: Int,
    var timeSlots: List<String>,
    var timings: List<Int>
): Serializable