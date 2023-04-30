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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding
import com.junting.drug_android_frontend.model.drug_record.DrugRecord
import com.junting.drug_android_frontend.ui.drugRecords.DrugRecordsViewModel

class DrugPositionButtonSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentPillBoxManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 使用 ViewBinding 綁定佈局檔案
        _binding = FragmentPillBoxManagementBinding.inflate(inflater, container, false)

        val ViewModel =
            ViewModelProvider(this).get(DrugRecordsViewModel::class.java)
        // 隱藏所有藥物位置
        for (i in 1..9) {
            initCell(i)
        }
        ViewModel.fetchRecordsByAll()
        ViewModel.records.observe(viewLifecycleOwner, {
            // 遍历记录并更新 UI
            for (record in it) {
                Log.d("PillBoxManagementFragment", "record: $record")
                when (record.position) {
                    1 -> showCell(1, record)
                    2 -> showCell(2, record)
                    3 -> showCell(3, record)
                    4 -> showCell(4, record)
                    5 -> showCell(5, record)
                    6 -> showCell(6, record)
                    7 -> showCell(7, record)
                    8 -> showCell(8, record)
                    9 -> showCell(9, record)
                }
            }
            for (i in 1..9) {
                closeProgressBar(i)
            }
        })


        return binding.root
    }
    fun closeProgressBar(drugPositionId: Int) {
        val drugPositionId = resources.getIdentifier(
            "ll_drug_position_$drugPositionId",
            "id",
            requireContext().packageName
        )
        val drugPositionView = binding.root.findViewById<View>(drugPositionId)
        drugPositionView?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
    }

    fun showCell(drugPositionId: Int, record: DrugRecord) {
        val drugPositionId = resources.getIdentifier(
            "ll_drug_position_$drugPositionId",
            "id",
            requireContext().packageName
        )
        val drugPositionView = binding.root.findViewById<View>(drugPositionId)
        drugPositionView?.findViewById<ImageView>(R.id.iv_drug_icon)?.visibility = View.VISIBLE
        drugPositionView?.findViewById<TextView>(R.id.tv_drug_name)?.visibility = View.VISIBLE
        drugPositionView?.findViewById<TextView>(R.id.chip_stock)?.visibility = View.VISIBLE
        drugPositionView?.findViewById<TextView>(R.id.tv_drug_name)?.text = record.drug.name
        drugPositionView?.findViewById<Chip>(R.id.chip_stock)?.text =
            "庫存: " + record.stock.toString()
        if (record.stock > 0) {
            drugPositionView?.findViewById<Chip>(R.id.chip_stock)?.setChipBackgroundColorResource(R.color.md_theme_light_secondaryContainer)
        } else {
            drugPositionView?.findViewById<Chip>(R.id.chip_stock)?.setChipBackgroundColorResource(R.color.md_theme_dark_error)
        }
        //設為不可被點選
        drugPositionView?.findViewById<View>(R.id.card_view)?.apply {
            isClickable = false
        }
    }
    fun resetCellsColor() {
        for (i in 1..9) {
            val drugPositionId = resources.getIdentifier(
                "ll_drug_position_$i",
                "id",
                requireContext().packageName
            )
            val drugPositionView = binding.root.findViewById<View>(drugPositionId)
            drugPositionView?.findViewById<CardView>(R.id.card_view)
                ?.setCardBackgroundColor(resources.getColor(android.R.color.background_light))
        }
    }

    fun initCell(drugPositionId: Int) {
        val drugPositionId = resources.getIdentifier(
            "ll_drug_position_$drugPositionId",
            "id",
            requireContext().packageName
        )
        val drugPositionView = binding.root.findViewById<View>(drugPositionId)
        drugPositionView?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE
        drugPositionView?.findViewById<ImageView>(R.id.iv_drug_icon)?.visibility = View.GONE
        drugPositionView?.findViewById<TextView>(R.id.tv_drug_name)?.visibility = View.GONE
        drugPositionView?.findViewById<TextView>(R.id.chip_stock)?.visibility = View.GONE
        drugPositionView?.findViewById<CardView>(R.id.card_view)?.setOnClickListener {
            val selectedDrugPositionIdNumber = resources.getResourceEntryName(drugPositionView.id).substringAfterLast("_")
            Log.d("CellClicked", "Cell ID: ${selectedDrugPositionIdNumber}")
            resetCellsColor()
            binding.root.findViewById<View>(drugPositionId).findViewById<CardView>(R.id.card_view)
                .setCardBackgroundColor(resources.getColor(R.color.md_theme_light_secondaryContainer))

        }
        drugPositionView?.findViewById<View>(R.id.card_view)?.setOnLongClickListener { view ->
            // 建立 MaterialAlertDialogBuilder 物件
            val builder = MaterialAlertDialogBuilder(requireContext())

            // 設定對話框標題
            builder.setTitle("提示")

            // 設定對話框訊息
            builder.setMessage("該格藥盒已開啟")

            // 設定PositiveButton按鈕，即確定按鈕
            builder.setPositiveButton("確定") { _, _ ->
                // 在這裡處理確定按鈕點擊事件
                // 可以執行對應的操作
            }


            // 建立並顯示 MaterialAlertDialogBuilder 物件
            val alertDialog = builder.create()
            alertDialog.show()

            // 返回 true 表示長按事件已被消費，不再傳遞給其他監聽器
            true
        }

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
