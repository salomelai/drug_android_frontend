package com.junting.drug_android_frontend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.ui.drugRecords.DrugRecordsViewModel

class DrugPositionButtonSheet(viewModel: DrugRecordsViewModel) : BottomSheetDialogFragment() {

    private var _binding: FragmentPillBoxManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrugRecordsViewModel
    private val positions = (1..9).toList()

    init {
        this.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // 使用 ViewBinding 綁定佈局檔案
        _binding = FragmentPillBoxManagementBinding.inflate(inflater, container, false)

        // 隱藏所有藥物位置
        for (i in positions) {
            initCell(i)
        }

        viewModel?.record?.observe(viewLifecycleOwner) {
            showCell(it.position, it)
            setCellColor(it.position)
            closeProgressBar(it.position)
        }

        viewModel.fetchRecordsByAll()
        viewModel.records.observe(viewLifecycleOwner) {
            it.toMutableList()
            // 遍历记录并更新 UI
            for (record in it) {
                if (record.position == viewModel.record.value?.position) continue //略過我現在正在編輯的這筆
                Log.d("DrugPositionButtonSheet", "record: $record")
                showCell(record.position, record)
            }
            for (i in positions) {
                closeProgressBar(i)
            }
        }

        return binding.root
    }

    private fun closeProgressBar(drugPositionId: Int) {
        val cellResourceId = getResourceIdByPosition(drugPositionId)
        val drugPositionView = binding.root.findViewById<View>(cellResourceId)
        drugPositionView?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
    }

    private fun showCell(drugPositionId: Int, record: DrugRecord) {
        val cellResourceId = getResourceIdByPosition(drugPositionId)
        val cellView = binding.root.findViewById<View>(cellResourceId)
        cellView?.findViewById<ImageView>(R.id.iv_drug_icon)?.visibility = View.VISIBLE
        cellView?.findViewById<TextView>(R.id.tv_drug_name)?.visibility = View.VISIBLE
        cellView?.findViewById<TextView>(R.id.chip_stock)?.visibility = View.VISIBLE
        cellView?.findViewById<TextView>(R.id.tv_drug_name)?.text = record.drug.name
        cellView?.findViewById<Chip>(R.id.chip_stock)?.text =
            "庫存: " + record.stock.toString()
        if (record.stock > 0) {
            cellView?.findViewById<Chip>(R.id.chip_stock)
                ?.setChipBackgroundColorResource(R.color.md_theme_light_secondaryContainer)
        } else {
            cellView?.findViewById<Chip>(R.id.chip_stock)
                ?.setChipBackgroundColorResource(R.color.md_theme_dark_error)
        }
        //設為不可被點選
        cellView?.findViewById<View>(R.id.card_view)?.apply {
            isClickable = false
        }
    }

    private fun resetCellsColor() {
        for (i in positions) {
            val cellResourceId = getResourceIdByPosition(i)
            val drugPositionView = binding.root.findViewById<View>(cellResourceId)
            drugPositionView?.findViewById<CardView>(R.id.card_view)
                ?.setCardBackgroundColor(resources.getColor(android.R.color.background_light))
        }
    }

    private fun setCellColor(drugPositionId: Int) {
        val cellResourceId = getResourceIdByPosition(drugPositionId)
        val cellView = binding.root.findViewById<View>(cellResourceId)
        cellView?.findViewById<CardView>(R.id.card_view)
            ?.setCardBackgroundColor(resources.getColor(R.color.md_theme_light_secondaryContainer))
    }

    private fun initCell(drugPosition: Int) {
        val drugPositionId = getResourceIdByPosition(drugPosition)
        val cellView = binding.root.findViewById<View>(drugPositionId)
        hideCell(cellView)
        cellView?.findViewById<CardView>(R.id.card_view)?.setOnClickListener {
            val selectedDrugPositionIdNumber =
                resources.getResourceEntryName(cellView.id).substringAfterLast("_")
            Log.d("CellClicked", "Cell ID: $selectedDrugPositionIdNumber")
            resetCellsColor()
            setCellColor(selectedDrugPositionIdNumber.toInt())
            val oldPosition = viewModel.record.value!!.position
            val oldCell = binding.root.findViewById<View>(getResourceIdByPosition(oldPosition))
            hideCell(oldCell)
            updateDrugRecordsPosition(oldPosition, drugPosition)
            viewModel.setPosition(drugPosition)
        }
    }

    private fun hideCell(cellView: View) {
        cellView?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE
        cellView?.findViewById<ImageView>(R.id.iv_drug_icon)?.visibility = View.GONE
        cellView?.findViewById<TextView>(R.id.tv_drug_name)?.visibility = View.GONE
        cellView?.findViewById<TextView>(R.id.chip_stock)?.visibility = View.GONE
    }

    private fun updateDrugRecordsPosition(oldPosition: Int, newPosition: Int) {
        val records = viewModel.records.value!!
        records.stream()
            .filter { it.position == oldPosition }
            .findFirst()
            .map { it.position = newPosition }
        viewModel.setRecords(records)
    }

    private fun getResourceIdByPosition(position: Int): Int {
        return resources.getIdentifier(
            "ll_drug_position_$position",
            "id",
            requireContext().packageName
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 解除 ViewBinding 的綁定
        _binding = null
    }

}
