package com.junting.drug_android_frontend.ui.drugRecords

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.DrugRecordActivity
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.databinding.DrugItemViewBinding
import com.junting.drug_android_frontend.model.drug_record.DrugRecord

class HospitalListViewAdapter(private val context: Context, private val viewModel: HospitalListViewModel) :
    RecyclerView.Adapter<HospitalListViewAdapter.MyViewHolder>() {

    class MyViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val button = Button(context)
        button.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return MyViewHolder(button)
    }

    override fun getItemCount(): Int {
        return viewModel.hospitals.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.button.text = viewModel.hospitals.value?.get(position)?.name
        holder.button.setOnClickListener {
            // Button click listener logic
        }
    }
}
