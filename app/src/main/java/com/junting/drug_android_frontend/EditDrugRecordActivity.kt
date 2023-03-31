package com.junting.drug_android_frontend

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.junting.drug_android_frontend.databinding.ActivityEditDrugRecordBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditDrugRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDrugRecordBinding

    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditDrugRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initExpandableListInteraction()
        initTimeSection()

    }

    private fun initTimeSection() {
        binding.tvTimeSlot.setOnClickListener {
            val parent = it.parent as ViewGroup
            parent.removeView(it)
        }
        binding.tvTimeSlotAdd.setOnClickListener{
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                val newTimeSlot = AppCompatTextView(this)
                newTimeSlot.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                newTimeSlot.background = ResourcesCompat.getDrawable(resources, android.R.drawable.list_selector_background, null)
                newTimeSlot.setPadding(10, 10, 10, 10)
                newTimeSlot.textSize = 16f
                newTimeSlot.text = time
                newTimeSlot.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_outline_delete_24, 0)
                newTimeSlot.compoundDrawablePadding = 15

                val parent = binding.tvTimeSlot.parent as ViewGroup
                parent.addView(newTimeSlot, parent.indexOfChild(binding.tvTimeSlot) + 1)

                newTimeSlot.setOnClickListener {
                    parent.removeView(newTimeSlot)
                }
            }, hour, minute, false)

            timePickerDialog.show()
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