package com.junting.drug_android_frontend.ui.todayReminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.junting.drug_android_frontend.databinding.RemindItemViewBinding
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import java.util.Collections

class TodayReminderViewAdapter2()
    :DragDropSwipeAdapter<TodayReminder, TodayReminderViewAdapter2.ViewHolder>(Collections.emptyList()) {

    class ViewHolder(private val binding: RemindItemViewBinding) : DragDropSwipeAdapter.ViewHolder(binding.root) {
        val tvDrugName: TextView = binding.tvDrugName
        val tvTime : TextView = binding.tvTime
        val tvDosage : TextView = binding.tvDosage
        val dragIcon: ImageView = binding.dragIcon

    }

    fun update(todayReminder: List<TodayReminder>) {
        dataSet = todayReminder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayReminderViewAdapter2.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RemindItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(item: TodayReminder, viewHolder: TodayReminderViewAdapter2.ViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        viewHolder.tvDrugName.text = item.drug.name
        viewHolder.tvTime.text = item.timeSlot
        viewHolder.tvDosage.text = "劑量: "+item.dosage.toString()
    }

    override fun getViewHolder(itemLayout: View): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getViewToTouchToStartDraggingItem(item: TodayReminder, viewHolder: TodayReminderViewAdapter2.ViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.dragIcon
    }
}