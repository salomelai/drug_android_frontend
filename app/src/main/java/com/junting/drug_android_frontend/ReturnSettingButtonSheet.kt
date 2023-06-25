package com.junting.drug_android_frontend

import DialogUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.junting.drug_android_frontend.databinding.BottomSheetReturnSettingBinding
import com.junting.drug_android_frontend.ui.drugRecords.DrugRecordsViewModel

class ReturnSettingButtonSheet(viewModel: DrugRecordsViewModel) :
    BottomSheetDialogFragment() {

    private var _binding: BottomSheetReturnSettingBinding? = null
    private val binding get() = _binding!!

    private var viewModel: DrugRecordsViewModel = viewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // 使用 ViewBinding 綁定佈局檔案
        _binding = BottomSheetReturnSettingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        DialogUtils.initTextViewEditDialog(
            requireContext(),
            binding.llLeft,
            binding.tvLeft,
            "修改剩餘庫存提醒",
            true
        ) { text ->
            viewModel.setReturnSettingLeft(text.toInt())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 解除 ViewBinding 的綁定
        _binding = null
    }
}
