package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.model.take_record.DateTakeRecordsRecord
import com.junting.drug_android_frontend.model.take_record.Medication
import com.junting.drug_android_frontend.ui.takeRecords.TakeRecordsByDrugRecyclerViewAdapter


class TakeRecordsByDrugExpandableListAdapter(
    private val context: Context,
    private val medications: List<Medication>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return medications.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return medications[groupPosition].dateTakeRecordsRecords.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return medications[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return medications[groupPosition].dateTakeRecordsRecords[childPosition]
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
        val medication = getGroup(groupPosition) as Medication
        // 根據需要自定義外層項目的佈局和內容
        // 返回外層項目的 View
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_1,
            parent,
            false
        )
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "     "+medication.name  //空白字符填充
        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val dateTakeRecordsRecord = getChild(groupPosition, childPosition) as DateTakeRecordsRecord
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
        val adapter = TakeRecordsByDrugRecyclerViewAdapter(context,dateTakeRecordsRecord)
        recyclerView.adapter = adapter

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
