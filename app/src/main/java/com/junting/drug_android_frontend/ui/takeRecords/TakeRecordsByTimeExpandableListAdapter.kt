package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.model.TakeRecord.DateTimeSlotRecord
import com.junting.drug_android_frontend.model.TakeRecord.TimeSlotRecord
import com.junting.drug_android_frontend.ui.takeRecords.TakeRecordsByTimeRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class TakeRecordsByTimeExpandableListAdapter(
    private val context: Context,
    private val dateTimeSlotRecords: List<DateTimeSlotRecord>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return dateTimeSlotRecords.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return dateTimeSlotRecords[groupPosition].timeSlotRecords.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return dateTimeSlotRecords[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return dateTimeSlotRecords[groupPosition].timeSlotRecords[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val dateTimeSlotRecord = getGroup(groupPosition) as DateTimeSlotRecord
        // 根據需要自定義外層項目的佈局和內容
        // 返回外層項目的 View
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_1,
            parent,
            false
        )
        val textView = view.findViewById<TextView>(android.R.id.text1)

        val date = dateTimeSlotRecord.date
        val inputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)

        val dayOfWeek = Calendar.getInstance().apply {
            time = parsedDate
        }.get(Calendar.DAY_OF_WEEK)

        val dayOfWeekString = when (dayOfWeek) {
            Calendar.MONDAY -> context.getString(R.string.monday)
            Calendar.TUESDAY -> context.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> context.getString(R.string.wednesday)
            Calendar.THURSDAY -> context.getString(R.string.thursday)
            Calendar.FRIDAY -> context.getString(R.string.friday)
            Calendar.SATURDAY -> context.getString(R.string.saturday)
            Calendar.SUNDAY -> context.getString(R.string.sunday)
            else -> ""
        }

        val finalText = "$dayOfWeekString $date"
        textView.text = "     "+finalText  //空白字符填充

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val timeSlotRecord = getChild(groupPosition, childPosition) as TimeSlotRecord
        // 根據需要自定義內層項目的佈局和內容
        // 返回內層項目的 View
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.take_records_inner_item_layout,
            parent,
            false
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Create and set the adapter for the RecyclerView
        val adapter = TakeRecordsByTimeRecyclerViewAdapter(context, timeSlotRecord.takeRecords)
        recyclerView.adapter = adapter

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
