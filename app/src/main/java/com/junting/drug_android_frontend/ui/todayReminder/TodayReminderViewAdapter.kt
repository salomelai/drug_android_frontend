package com.junting.drug_android_frontend.ui.todayReminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.junting.drug_android_frontend.databinding.RemindItemViewBinding
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import java.text.SimpleDateFormat
import java.util.*

class TodayReminderViewAdapter()
    :DragDropSwipeAdapter<TodayReminder, TodayReminderViewAdapter.ViewHolder>(Collections.emptyList()) {

    class ViewHolder(private val binding: RemindItemViewBinding) : DragDropSwipeAdapter.ViewHolder(binding.root) {
        val viewColor = binding.viewColor
        val tvDrugName: TextView = binding.tvDrugName
        val tvTime : TextView = binding.tvTime
        val tvDosage : TextView = binding.tvDosage
        val dragIcon: ImageView = binding.dragIcon

    }

    fun update(todayReminder: List<TodayReminder>) {
        dataSet = todayReminder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayReminderViewAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RemindItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(item: TodayReminder, viewHolder: TodayReminderViewAdapter.ViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        viewHolder.tvDrugName.text = item.drug.name
        viewHolder.tvTime.text = item.timeSlot
        viewHolder.tvDosage.text = "劑量: "+item.dosage.toString()

        val currentTime = Calendar.getInstance()
        val currentTimeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime.time)

        val timeSlot = item.timeSlot

        if (currentTimeString >= timeSlot) {
            viewHolder.viewColor.setBackgroundResource(com.google.android.material.R.color.design_default_color_error)
        } else {
            viewHolder.viewColor.visibility = View.INVISIBLE
        }
    }

    override fun getViewHolder(itemLayout: View): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getViewToTouchToStartDraggingItem(item: TodayReminder, viewHolder: TodayReminderViewAdapter.ViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.dragIcon
    }
}