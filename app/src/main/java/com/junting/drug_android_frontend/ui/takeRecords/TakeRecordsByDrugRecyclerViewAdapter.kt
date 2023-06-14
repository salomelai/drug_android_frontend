package com.junting.drug_android_frontend.ui.takeRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.model.TakeRecord.DateRecord
import com.junting.drug_android_frontend.model.TakeRecord.TakeRecord

class TakeRecordsByDrugRecyclerViewAdapter(
    private val context: Context,
    private val dateRecord: DateRecord) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATE = 0
        private const val VIEW_TYPE_TAKE_RECORD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_DATE
        } else {
            VIEW_TYPE_TAKE_RECORD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATE -> {
                val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
                DateViewHolder(view)
            }
            VIEW_TYPE_TAKE_RECORD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.take_records_by_drug_item_view, parent, false)
                TakeRecordViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> {
                holder.bind(dateRecord.date)
            }
            is TakeRecordViewHolder -> {
                val takeRecord = dateRecord.takeRecords[position - 1]
                holder.bind(takeRecord)
            }
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemCount(): Int {
        // Add 1 for the date item
        return dateRecord.takeRecords.size + 1
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(date: String) {
            dateTextView.text = date
        }
    }

    inner class TakeRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dosageTextView: TextView = itemView.findViewById(R.id.tv_dosage)
        private val timeSlotTextView: TextView = itemView.findViewById(R.id.tv_time_slot)
        private val statusImageView: ImageView = itemView.findViewById(R.id.iv_status)

        fun bind(takeRecord: TakeRecord) {
            // 根據需要設置藥物記錄項目的內容
            dosageTextView.text = takeRecord.dosage.toString()+"顆"
            timeSlotTextView.text = takeRecord.timeSlot
            if (takeRecord.status == 0) {
                statusImageView.setImageResource(R.drawable.ic_baseline_question_mark_24)
            } else if (takeRecord.status == 1) {
                timeSlotTextView.setTextColor(ContextCompat.getColor(context, R.color.md_theme_light_secondaryContainer))
                statusImageView.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }else{
                timeSlotTextView.setTextColor(ContextCompat.getColor(context, R.color.md_theme_dark_error))
                statusImageView.setImageResource(R.drawable.ic_baseline_cancel_24)
            }
        }
    }
}

