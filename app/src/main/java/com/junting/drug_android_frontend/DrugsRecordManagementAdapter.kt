package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.DrugItemViewBinding
import com.junting.drug_android_frontend.model.Record

class DrugsRecordManagementAdapter(private val context: Context, private val viewModel: DrugsViewModel) :
    RecyclerView.Adapter<DrugsRecordManagementAdapter.MyViewHolder>() {

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
        val record: Record = viewModel.records.value!!.get(position)
        holder.drugItemViewBinding.tvDrugName.text = record.drug.name
        holder.drugItemViewBinding.tvDosage.text = record.dosage.toString()
        holder.drugItemViewBinding.tvStock.text = record.stock.toString()

        holder.drugItemViewBinding.layoutItem.setOnClickListener {
            Toast.makeText(context, String.format("You clicked %s", record.drug.name), Toast.LENGTH_SHORT).show()
        }
    }

}