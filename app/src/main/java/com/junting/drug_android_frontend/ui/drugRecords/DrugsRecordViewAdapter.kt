package com.junting.drug_android_frontend.ui.drugRecords

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.DrugItemViewBinding
import com.junting.drug_android_frontend.model.DrugRecord

class DrugsRecordViewAdapter(private val context: Context, private val viewModel: DrugRecordsViewModel) :
    RecyclerView.Adapter<DrugsRecordViewAdapter.MyViewHolder>() {

    class MyViewHolder(val drugItemViewBinding: DrugItemViewBinding) :
        RecyclerView.ViewHolder(drugItemViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val drugItemViewBinding =
            DrugItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(drugItemViewBinding)
    }

    override fun getItemCount(): Int {
        return viewModel.records.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drugRecord: DrugRecord = viewModel.records.value!!.get(position)
        holder.drugItemViewBinding.tvDrugName.text = drugRecord.drug.name
        holder.drugItemViewBinding.tvIndication.text = drugRecord.drug.indications
        holder.drugItemViewBinding.tvFrequencyDosage.text = drugRecord.frequency.toString() + " times a day, " + drugRecord.dosage.toString() + " pills each time"
        var timeSlotLine = ""
        for(timeSlot in drugRecord.timeSlot) {
            timeSlotLine += timeSlot + ", "
        }
        holder.drugItemViewBinding.tvTimeSlot.text = timeSlotLine
        holder.drugItemViewBinding.tvHospitalDepartment.text = drugRecord.hospitalName.toString() + ", " + drugRecord.hospitalDepartment.toString()

        holder.drugItemViewBinding.layoutItem.setOnClickListener {
            Toast.makeText(context, String.format("You clicked %s", drugRecord.drug.name), Toast.LENGTH_SHORT).show()
        }
    }

}