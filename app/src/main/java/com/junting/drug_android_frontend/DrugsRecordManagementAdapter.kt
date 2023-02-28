package com.junting.drug_android_frontend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.DrugItemViewBinding
import com.junting.drug_android_frontend.model.Drug

class DrugsRecordManagementAdapter(private val itemClickListener: DrugsRecordManagementActivity) :
    RecyclerView.Adapter<DrugsRecordManagementAdapter.MyViewHolder>() {

    var drugsList: List<Drug> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface IItemClickListener {
        fun onItemClickListener(data: Drug)
    }

    class MyViewHolder(val drugItemViewBinding: DrugItemViewBinding) :
        RecyclerView.ViewHolder(drugItemViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val drugItemViewBinding =
            DrugItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(drugItemViewBinding)
    }

    override fun getItemCount(): Int {
        return drugsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.drugItemViewBinding.tvDrugName.text = drugsList[position].drug_name
        holder.drugItemViewBinding.tvDosage.text = drugsList[position].dosage.toString()
        holder.drugItemViewBinding.tvStock.text = drugsList[position].stock.toString()

        holder.drugItemViewBinding.layoutItem.setOnClickListener {
            itemClickListener.onItemClickListener(drugsList[position])
        }
    }

}