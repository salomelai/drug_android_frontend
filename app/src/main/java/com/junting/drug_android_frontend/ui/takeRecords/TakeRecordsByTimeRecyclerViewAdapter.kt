package com.junting.drug_android_frontend.ui.takeRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.model.TakeRecord.TakeRecord

class TakeRecordsByTimeRecyclerViewAdapter(
    private val context: Context,
    private val takeRecords: List<TakeRecord>
) : RecyclerView.Adapter<TakeRecordsByTimeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.take_records_by_time_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val takeRecord = takeRecords[position]
        holder.bind(takeRecord)
    }

    override fun getItemCount(): Int {
        return takeRecords.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val drugNameTextView: TextView = itemView.findViewById(R.id.tv_drug_name)
        private val dosageTextView: TextView = itemView.findViewById(R.id.tv_dosage)
        private val timeSlotTextView: TextView = itemView.findViewById(R.id.tv_time_slot)

        fun bind(takeRecord: TakeRecord) {
            drugNameTextView.text = takeRecord.drug.name
            dosageTextView.text = takeRecord.dosage.toString()
            timeSlotTextView.text = takeRecord.timeSlot
        }
    }
}

