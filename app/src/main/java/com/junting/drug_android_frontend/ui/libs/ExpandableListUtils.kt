package com.junting.drug_android_frontend.ui.libs

import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView

class ExpandableListUtils {
    companion object {
        fun setupExpandHeight(view: ExpandableListView, adapter: ExpandableListAdapter) {
            view.setOnGroupExpandListener(ExpandableListView.OnGroupExpandListener { groupPosition ->
                var totalHeight = 0
                val desiredWidth = View.MeasureSpec.makeMeasureSpec(
                    view.getWidth(),
                    View.MeasureSpec.EXACTLY
                )
                for (i in 0 until adapter.getChildrenCount(groupPosition)) {
                    val listItem: View = adapter.getChildView(groupPosition, i, false, null, view)
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
                val params: ViewGroup.LayoutParams = view.getLayoutParams()
                params.height = view.getPaddingTop() + view.getPaddingBottom() + totalHeight +
                                view.getDividerHeight() * (adapter.getChildrenCount(groupPosition) - 1) + 140
                // extra height 140 to workaround solve height difference
                view.setLayoutParams(params)
            })

            view.setOnGroupCollapseListener(ExpandableListView.OnGroupCollapseListener {
                val params: ViewGroup.LayoutParams = view.getLayoutParams()
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                view.setLayoutParams(params)
            })
        }
    }
}