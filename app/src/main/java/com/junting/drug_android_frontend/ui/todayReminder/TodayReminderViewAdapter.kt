package com.junting.drug_android_frontend.ui.todayReminder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.RemindItemViewBinding
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder

class TodayReminderViewAdapter(private val context: Context ,private val viewModel: TodayReminderViewModel) :
    RecyclerView.Adapter<TodayReminderViewAdapter.MyViewHolder>() {

    class MyViewHolder(val ViewBinding: RemindItemViewBinding) :
        RecyclerView.ViewHolder(ViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val ViewBinding =
            RemindItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(ViewBinding)
    }

    override fun getItemCount(): Int {
        return viewModel.records.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todayReminder: TodayReminder = viewModel.records.value!!.get(position)
        holder.ViewBinding.tvDrugName.text = todayReminder.drug.name
        holder.ViewBinding.tvTime.text = todayReminder.timeSlot
        holder.ViewBinding.tvDosage.text = todayReminder.dosage.toString()+"單位"
    }

}