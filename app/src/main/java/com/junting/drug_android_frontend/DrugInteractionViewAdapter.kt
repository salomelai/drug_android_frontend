package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.DrugInteractionViewBinding

class DrugInteractionViewAdapter(
    private val context: Context,
    private val viewModel: DrugInteractionViewModel,
) :
    RecyclerView.Adapter<DrugInteractionViewAdapter.MyViewHolder>() {

    class MyViewHolder(val drugInteractionViewBinding: DrugInteractionViewBinding) :
        RecyclerView.ViewHolder(drugInteractionViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val drugInteractionViewBinding =
            DrugInteractionViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(drugInteractionViewBinding)
    }

    override fun getItemCount(): Int {
        return viewModel.drugInteractions.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val interactingDrugs = viewModel.drugInteractions.value!!.get(position)
        holder.drugInteractionViewBinding.tvDrugName.text = interactingDrugs.name
        holder.drugInteractionViewBinding.tvDegree.text = interactingDrugs.degree
        holder.drugInteractionViewBinding.tvCause.text = interactingDrugs.cause
    }


}

