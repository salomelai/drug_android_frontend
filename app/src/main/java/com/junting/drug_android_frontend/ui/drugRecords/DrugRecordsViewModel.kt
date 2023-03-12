package com.junting.drug_android_frontend.ui.drugRecords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrugRecordsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Personal Records Fragment"
    }
    val text: LiveData<String> = _text
}