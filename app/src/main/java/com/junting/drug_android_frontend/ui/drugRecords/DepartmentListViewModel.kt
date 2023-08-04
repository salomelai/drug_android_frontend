package com.junting.drug_android_frontend.ui.drugRecords

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.model.Department
import com.junting.drug_android_frontend.services.IDepartmentService
import com.junting.drug_android_frontend.services.IHospitalService
import kotlinx.coroutines.launch

class DepartmentListViewModel : ViewModel() {

    val departments = MutableLiveData<List<Department>>()


    fun fetchRecords() {
        viewModelScope.launch {
            val departmentService = IDepartmentService.getInstance()
            try {
                departments.value = departmentService.getDepartments()
            } catch (e: Exception) {
                Log.d("DepartmentListViewModel", e.toString())
                Log.e("DepartmentListViewModel", e.stackTraceToString())
            }
        }
    }
}