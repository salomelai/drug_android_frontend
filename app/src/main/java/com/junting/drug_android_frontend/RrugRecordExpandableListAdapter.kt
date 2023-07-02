package com.junting.drug_android_frontend

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.junting.drug_android_frontend.model.drug_record.InteractingDrug


class RrugRecordExpandableListAdapter internal constructor(
    private val context: Context,
    private val interactingDrugs: List<InteractingDrug>
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return interactingDrugs.get(expandedListPosition).id.toString()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null)
        {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.drug_interaction_view, null)
        }

        val tvDrugName = convertView!!.findViewById<TextView>(R.id.tv_drug_name)
        tvDrugName.text = interactingDrugs.get(expandedListPosition).name

        val tvCause = convertView!!.findViewById<TextView>(R.id.tv_cause)
        tvCause.text = interactingDrugs.get(expandedListPosition).cause

        val tvDegree = convertView!!.findViewById<TextView>(R.id.tv_degree)
        tvDegree.text = interactingDrugs.get(expandedListPosition).degree
        if (tvDegree.text == "Major") {
            tvDegree.setTextColor(context.getColor(com.google.android.material.R.color.design_default_color_error))
        } else if (tvDegree.text == "Moderate") {
            tvDegree.setTextColor(context.getColor(com.google.android.material.R.color.design_default_color_primary))
        }else if (tvDegree.text == "Minor") {
            tvDegree.setTextColor(context.getColor(com.google.android.material.R.color.design_default_color_secondary))
        }

        val tvTimeSlots = convertView!!.findViewById<TextView>(R.id.tv_time_slots)
        if(interactingDrugs.get(expandedListPosition).timeSlots.isEmpty()) {
            tvTimeSlots.text = context.getString(R.string.as_needed)
        } else{
            tvTimeSlots.text = interactingDrugs.get(expandedListPosition).timeSlots.joinToString()
        }

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return interactingDrugs.size
    }

    override fun getGroup(listPosition: Int): Any {
        return "交互作用"
    }

    override fun getGroupCount(): Int {
        return 1
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null)
        {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.interaction_expandable_group_item, null)
        }

        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle

        val expandIndicator = convertView.findViewById<ImageView>(R.id.expandIndicator)
        if (isExpanded) {
            expandIndicator.setImageResource(R.drawable.ic_baseline_expand_less_24)
        } else {
            expandIndicator.setImageResource(R.drawable.ic_baseline_expand_more_24)
        }

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
