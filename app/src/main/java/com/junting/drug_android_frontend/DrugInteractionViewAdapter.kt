package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R
import com.junting.drug_android_frontend.databinding.DrugInteractionViewBinding
//import com.junting.drug_android_frontend.R

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
        val interactingDrug = viewModel.drugInteractions.value!!.get(position)
        holder.drugInteractionViewBinding.tvDrugName.text = interactingDrug.name

        holder.drugInteractionViewBinding.tvDegree.text = interactingDrug.degree
        if (interactingDrug.degree == "Major") {
            holder.drugInteractionViewBinding.tvDegree.setTextColor(context.getColor(R.color.design_default_color_error))
        } else if (interactingDrug.degree == "Moderate") {
            holder.drugInteractionViewBinding.tvDegree.setTextColor(context.getColor(R.color.design_default_color_primary))
        }else if (interactingDrug.degree == "Minor") {
            holder.drugInteractionViewBinding.tvDegree.setTextColor(context.getColor(R.color.design_default_color_secondary))
        }

        holder.drugInteractionViewBinding.tvCause.text = interactingDrug.cause
        if(interactingDrug.timeSlots.isEmpty()) {
            holder.drugInteractionViewBinding.tvTimeSlots.text = context.getString(R.string.as_needed)
        } else{
            holder.drugInteractionViewBinding.tvTimeSlots.text = interactingDrug.timeSlots.joinToString()
        }

    }
}

