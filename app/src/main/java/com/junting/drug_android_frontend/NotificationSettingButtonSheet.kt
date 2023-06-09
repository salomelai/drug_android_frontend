package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.junting.drug_android_frontend.databinding.BottomSheetNotificationSettingBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationSettingButtonSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetNotificationSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 使用 ViewBinding 綁定佈局檔案
        _binding = BottomSheetNotificationSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 在這裡進行 View 的初始化和事件設置
        binding.llStartDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 解除 ViewBinding 的綁定
        _binding = null
    }

    private fun showDatePickerDialog() {
        // 使用 MaterialDatePicker 顯示日期選擇對話框
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        // 設置選擇日期後的回調
        picker.addOnPositiveButtonClickListener { selection ->
            // 取得選擇的日期
            val selectedDate = Date(selection)

            // 格式化日期
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            // 更新 tv_start_date 的文字
            binding.tvStartDate.text = formattedDate
        }

        // 顯示 MaterialDatePicker 對話框
        picker.show(parentFragmentManager, "datePicker")
    }
}
