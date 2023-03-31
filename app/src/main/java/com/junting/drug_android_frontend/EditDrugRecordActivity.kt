package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ExpandableListAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.junting.drug_android_frontend.databinding.ActivityEditDrugRecordBinding

class EditDrugRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDrugRecordBinding

    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditDrugRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llDrugName.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("修改藥物名稱")

            // 建立一個 EditText 供使用者輸入新的藥物名稱
            val input = EditText(this)
            input.setText(binding.tvDrugName.text)
            builder.setView(input)

            // 設定確認和取消按鈕
            builder.setPositiveButton("確定") { dialog, which ->
                binding.tvDrugName.text = input.text.toString()
            }
            builder.setNegativeButton("取消") { dialog, which ->
                dialog.cancel()
            }

            // 顯示對話框
            builder.show()
        }

        initExpandableListInteraction()
        initTimeSection()
        initOndemandCheckbox()
        initTimingsCheckbox()

    }

    private fun initTimingsCheckbox() {
        binding.cbBeforeMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbAfterMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbAfterMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbWithFood.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbAfterMeal.isEnabled = !isChecked
        }
    }

    private fun initOndemandCheckbox() {
        binding.llOnDemand.setOnClickListener {
            binding.cbOnDemand.isChecked = !binding.cbOnDemand.isChecked
        }
        binding.cbOnDemand.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.llTimeSlot.visibility = View.GONE
                binding.llTimings.visibility = View.GONE
            } else {
                binding.llTimeSlot.visibility = View.VISIBLE
                binding.llTimings.visibility = View.VISIBLE
            }
        }
    }

    private fun initTimeSection() {
        binding.tvTimeSlot.setOnClickListener {
            val parent = it.parent as ViewGroup
            parent.removeView(it)
        }
        binding.tvTimeSlotAdd.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select time")
                .build()
            timePicker.show(supportFragmentManager, "time_picker")
            timePicker.addOnPositiveButtonClickListener {
                val time = "${timePicker.hour}:${timePicker.minute}"
            }
        }
    }
    private fun initExpandableListInteraction() {
        val listData = data
        titleList = ArrayList(listData.keys)
        adapter = EditRrugExpandableListAdapter(this, titleList as ArrayList<String>, listData)
        binding.expandableListInteraction!!.setAdapter(adapter)
    }

    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()

            val interactionGroup = ArrayList<String>()
            interactionGroup.add("Acertil")
            interactionGroup.add("Rifampin")


            // set multiple list to header title position
            listData["交互作用"] = interactionGroup

            return listData
        }


}