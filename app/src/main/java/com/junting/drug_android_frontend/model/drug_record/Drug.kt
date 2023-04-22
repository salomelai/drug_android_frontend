package com.junting.drug_android_frontend.model.drug_record

import java.io.Serializable

data class Drug(
    var appearance: String,
    var id: Int,
    var indication: String,
    var name: String,
    var sideEffect: String
): Serializable