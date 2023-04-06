package com.junting.drug_android_frontend

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.junting.drug_android_frontend.databinding.ActivityEditDrugRecordBinding
import com.junting.drug_android_frontend.ui.libs.ExpandableListUtils
import java.util.*

class EditDrugRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDrugRecordBinding

    internal var adapter: EditRrugExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    private lateinit var viewModel: EditDrugRecordViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditDrugRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var times = mutableListOf("10:00", "12:00", "14:00") // 假設這是您的時間點列表

        for (timeSlot in times) {
            val tvTimeSlot = AppCompatTextView(this)
            tvTimeSlot.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
//            tvTimeSlot.background = ContextCompat.getDrawable(this, android.R.attr.selectableItemBackground)
            val iconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_outline_delete_24)
            tvTimeSlot.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, iconDrawable, null)
            tvTimeSlot.setPadding(10, 10, 10, 10)
            tvTimeSlot.text = timeSlot
            tvTimeSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            binding.llTimeSlot.addView(tvTimeSlot)
            tvTimeSlot.setOnClickListener {
                times.remove(timeSlot)
                binding.llTimeSlot.removeView(tvTimeSlot)
            }
        }
        binding.tvTimeSlotAdd.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .build()

            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour.toString().padStart(2, '0')
                val minute = picker.minute.toString().padStart(2, '0')
                val time = "$hour:$minute"

                // check if the time already exists
                if (times.contains(time)) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Warning")
                        .setMessage("The selected time already exists.")
                        .setPositiveButton("OK", null)
                        .show()
                    return@addOnPositiveButtonClickListener
                }

                // add the new time to the list
                times.add(time)

                // sort the time list
                Collections.sort(times)

                // remove all the views in the LinearLayout
                binding.llTimeSlot.removeAllViews()

                // add all the time slots again to the LinearLayout
                for (timeSlot in times) {
                    val tvTimeSlot = AppCompatTextView(this)
                    tvTimeSlot.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val iconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_outline_delete_24)
                    tvTimeSlot.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, iconDrawable, null)
                    tvTimeSlot.setPadding(10, 10, 10, 10)
                    tvTimeSlot.text = timeSlot
                    tvTimeSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    binding.llTimeSlot.addView(tvTimeSlot)
                    tvTimeSlot.setOnClickListener {
                        times.remove(timeSlot)
                        binding.llTimeSlot.removeView(tvTimeSlot)
                    }
                }
            }

            picker.show(supportFragmentManager, "time_picker")
        }


        initDrugNameTextView()
        initExpandableListInteraction()
//        initTimeSection()
        initOndemandCheckbox()
        initTimingsCheckbox()
        initDrugRecordViewModel()

    }

    private fun initDrugRecordViewModel(){
        binding.progressBar.visibility = View.VISIBLE
        viewModel = EditDrugRecordViewModel()
        viewModel.fetchRecord(20)
        viewModel.record.observe(this, Observer {
            binding.tvDrugName.text = it.drug.name
            binding.tvHospital.text = it.hospitalName
            binding.tvDepartment.text = it.hospitalDepartment
            binding.tvIndication.text = it.drug.indications
            binding.tvSideEffect.text = it.drug.sideEffect
            binding.tvAppearance.text = it.drug.appearance
            binding.cbOnDemand.isChecked = it.onDemand

            binding.progressBar.visibility = View.GONE

        })
    }

    private fun initDrugNameTextView(){
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

//    private fun initTimeSection() {
//        binding.tvTimeSlot.setOnClickListener {
//            val parent = it.parent as ViewGroup
//            parent.removeView(it)
//        }
//        binding.tvTimeSlotAdd.setOnClickListener {
//            val timePicker = MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_24H)
//                .setHour(12)
//                .setMinute(10)
//                .setTitleText("Select time")
//                .build()
//            timePicker.show(supportFragmentManager, "time_picker")
//            timePicker.addOnPositiveButtonClickListener {
//                val time = "${timePicker.hour}:${timePicker.minute}"
//            }
//        }
//    }
    private fun initExpandableListInteraction() {
        val listData = data
        titleList = ArrayList(listData.keys)
        adapter = EditRrugExpandableListAdapter(this, titleList as ArrayList<String>, listData)
        binding.expandableListInteraction!!.setAdapter(adapter)
        ExpandableListUtils.setupExpandHeight(binding.expandableListInteraction!!, adapter!!)
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