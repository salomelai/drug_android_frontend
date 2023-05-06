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
        _binding = FragmentPillBoxManagementBinding.inflate(inflater, container, false)

        positions.forEach { i -> initCell(i) }
        viewModel.record.observe(viewLifecycleOwner) {
            if (positions.contains(it.position)) {
                showCell(it.position, it)
                setCellColor(it.position)
                closeProgressBar(it.position)
            }
        }
        if (viewModel.records.value == null) {
            viewModel.fetchRecordsByAll()
        }
        viewModel.records.observe(viewLifecycleOwner) {
            it.toMutableList()
            it.stream()
                // skip current editing record
                .filter { record -> record.position != viewModel.record.value?.position }
                .filter { record -> positions.contains(record.position) }
                .forEach { record -> showCell(record.position, record) }
            positions.forEach { i -> closeProgressBar(i) }
        }
        return binding.root
    }

    private fun closeProgressBar(drugPositionId: Int) {
        val cellResourceId = getResourceIdByPosition(drugPositionId)
        val drugPositionView = binding.root.findViewById<View>(cellResourceId)
        drugPositionView?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
    }

    private fun showCell(position: Int, record: DrugRecord) {
        val cellResourceId = getResourceIdByPosition(position)
        val cellView = binding.root.findViewById<View>(cellResourceId)
        val drugIcon = cellView?.findViewById<ImageView>(R.id.iv_drug_icon)!!
        val chipStock = cellView.findViewById<Chip>(R.id.chip_stock)!!
        val drugName = cellView.findViewById<TextView>(R.id.tv_drug_name)!!
        drugIcon.visibility = View.VISIBLE
        drugName.visibility = View.VISIBLE
        chipStock.visibility = View.VISIBLE
        drugName.text = record.drug.name
        chipStock.text = "庫存: " + record.stock.toString()
        if (record.stock > 0) {
            chipStock.setChipBackgroundColorResource(R.color.md_theme_light_secondaryContainer)
        } else {
            chipStock.setChipBackgroundColorResource(R.color.md_theme_dark_error)
        }
    }

    private fun hideCell(position: Int) {
        if (positions.contains(position)) {
            val drugPositionId = getResourceIdByPosition(position)
            val cellView = binding.root.findViewById<View>(drugPositionId)
            cellView.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE
            cellView.findViewById<ImageView>(R.id.iv_drug_icon)?.visibility = View.GONE
            cellView.findViewById<TextView>(R.id.tv_drug_name)?.visibility = View.GONE
            cellView.findViewById<TextView>(R.id.chip_stock)?.visibility = View.GONE
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

    private fun setCellColor(position: Int) {
        val cellResourceId = getResourceIdByPosition(position)
        val cellView = binding.root.findViewById<View>(cellResourceId)
        cellView?.findViewById<CardView>(R.id.card_view)
            ?.setCardBackgroundColor(resources.getColor(R.color.md_theme_light_secondaryContainer))
    }

    private fun initCell(position: Int) {
        val drugPositionId = getResourceIdByPosition(position)
        val cellView = binding.root.findViewById<View>(drugPositionId)
        hideCell(position)
        cellView?.findViewById<CardView>(R.id.card_view)?.setOnClickListener {
            if (!isPositionEmpty(position)) {
                return@setOnClickListener
            }
            val selectedDrugPositionIdNumber =
                resources.getResourceEntryName(cellView.id).substringAfterLast("_")
            Log.d("CellClicked", "Cell ID: $selectedDrugPositionIdNumber")
            resetCellsColor()
            setCellColor(selectedDrugPositionIdNumber.toInt())
            val oldPosition = viewModel.record.value!!.position
            hideCell(oldPosition)
            updateDrugRecordsPosition(oldPosition, position)
            viewModel.setPosition(position)
        }
    }

    private fun updateDrugRecordsPosition(oldPosition: Int, newPosition: Int) {
        val records = viewModel.records.value!!
        records.stream()
            .filter { it.position == oldPosition }
            .findFirst()
            .map { it.position = newPosition }
        viewModel.setRecords(records)
    }

    private fun isPositionEmpty(position: Int): Boolean {
        return viewModel.records.value!!.stream().noneMatch { it.position == position }
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
