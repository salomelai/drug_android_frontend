package com.junting.drug_android_frontend.ui.personalRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.junting.drug_android_frontend.R

class PersonalRecordsPagerAdapter(context: Context): PagerAdapter() {

    private val context: Context

    init {
        this.context = context
    }

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position == 0) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.personal_records_all_tab, container, false)
            container.addView(view)
            return view
        } else if (position == 1) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.personal_records_hospital_tab, container, false)
            container.addView(view)
            return view
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.personal_records_department_tab, container, false)
            container.addView(view)
            return view
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}