package com.junting.drug_android_frontend

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.ResponseMessage
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.model.take_record.TakeRecord
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import com.junting.drug_android_frontend.services.IDrugRecordService
import com.junting.drug_android_frontend.services.ITakeRecordService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OnDemandViewModel : ViewModel() {

    var drugRecord = MutableLiveData<DrugRecord>()
    var drugRecors = MutableLiveData<List<DrugRecord>>()
    var onDemandDrugRecors = MutableLiveData<List<DrugRecord>>()
    val actualTakingTime  = ObservableField<String>()

    fun setDosage(dosage: Int) {
        val info: DrugRecord = drugRecord.value!!
        info.dosage = dosage
        triggerUpdate(info)
    }

    fun setActualTakingTime(actualTakingTime: String) {
        this.actualTakingTime.set(actualTakingTime)
    }

    private fun triggerUpdate(newDrugRecord: DrugRecord) {
        drugRecord.value = newDrugRecord
    }

    fun fetchDrugRecordById(Id:Int) {
        viewModelScope.launch {
            val drugRecordService = IDrugRecordService.getInstance()
            try {
                drugRecord.value = drugRecordService.getDrugById(Id)
            } catch (e: Exception) {
                Log.d("OnDemandViewModel", "fetch drugRecord failed")
                Log.e("OnDemandViewModel", e.toString())
            }
        }
    }
    fun fetchDrugRecords() {
        viewModelScope.launch {
            val drugRecordService = IDrugRecordService.getInstance()
            try {
                drugRecors.value = drugRecordService.getDrugs()
            } catch (e: Exception) {
                Log.d("OnDemandViewModel", "fetch drugRecords failed")
                Log.e("OnDemandViewModel", e.toString())
            }
        }
    }
    fun fetchDrugRecordByOnDemand(onDemand: Boolean) {
        viewModelScope.launch {
            val drugRecordService = IDrugRecordService.getInstance()
            try {
                onDemandDrugRecors.value = drugRecordService.getDrugsByOnDemand(onDemand)
            } catch (e: Exception) {
                Log.d("OnDemandViewModel", "fetch onDemandDrugRecors failed")
                Log.e("OnDemandViewModel", e.toString())
            }
        }
    }
    fun processTakeRecord(takeRecord: TakeRecord): Deferred<ResponseMessage?> {
        return viewModelScope.async {
            val takeRecordService = ITakeRecordService.getInstance()
            try {
                val response = takeRecordService.processTakeRecord(takeRecord)
                if (response.isSuccessful) {
                    val message = response.body()
                    if (message != null) {
                        Log.d("OnDemandViewModel", "process takeRecord success")
                        message
                    } else {
                        // 处理ResponseMessage为null的情况
                        throw IllegalStateException("Response body is null")
                    }
                } else {
                    Log.d("OnDemandViewModel", "process takeRecord failed")
                    null
                }
            } catch (e: Exception) {
                // 处理异常
                Log.d("OnDemandViewModel", "process TakeRecord failed")
                Log.e("OnDemandViewModel", e.toString())
                null
            }
        }
    }

}