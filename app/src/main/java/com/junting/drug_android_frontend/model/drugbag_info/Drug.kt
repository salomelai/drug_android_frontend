package com.junting.drug_android_frontend.model.drugbag_info

import java.io.Serializable

data class Drug(
    var appearance: String = "",
    var id: Int = 0,
    var indication: String = "",
    var name: String = "",
    var sideEffect: String = ""
): Serializable