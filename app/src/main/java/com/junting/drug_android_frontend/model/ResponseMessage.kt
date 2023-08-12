package com.junting.drug_android_frontend.model

data class ResponseMessage (
    var success : Boolean,
    var errorCode : Int,
    var positions : List<Int>,
)