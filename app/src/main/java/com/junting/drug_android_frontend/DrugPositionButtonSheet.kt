package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.junting.drug_android_frontend.databinding.FragmentNotificationSettingBinding
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding
import com.junting.drug_android_frontend.ui.pillBoxManagement.PillBoxManagementFragment
import java.text.SimpleDateFormat
import java.util.*

class DrugPositionButtonSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentPillBoxManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 使用 ViewBinding 綁定佈局檔案
        _binding = FragmentPillBoxManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 解除 ViewBinding 的綁定
        _binding = null
    }

}
