package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.DrugItemViewBinding
import com.junting.drug_android_frontend.model.Drug

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
        return viewModel.drugs.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drag: Drug = viewModel.drugs.value!!.get(position)
        holder.drugItemViewBinding.tvDrugName.text = drag.drug_name
        holder.drugItemViewBinding.tvDosage.text = drag.dosage.toString()
        holder.drugItemViewBinding.tvStock.text = drag.stock.toString()

        holder.drugItemViewBinding.layoutItem.setOnClickListener {
            Toast.makeText(context, String.format("You clicked %s", drag.drug_name), Toast.LENGTH_SHORT).show()
        }
    }

}