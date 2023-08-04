package com.junting.drug_android_frontend.ui.drugRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.junting.drug_android_frontend.R

class DrugRecordsPagerAdapter(context: Context): PagerAdapter() {

    private val context: Context
    private var allPage: DrugRecordsAllPage? = null
    private var hospitalPage: DrugRecordsHospitalPage? = null
    private var departmentPage: DrugRecordsDepartmentPage? = null

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
            allPage = DrugRecordsAllPage(context, container)
            container.addView(allPage!!.view)
            return allPage!!.view
        } else if (position == 1) {
            hospitalPage = DrugRecordsHospitalPage(context, container)
            container.addView(hospitalPage!!.view)
            return hospitalPage!!.view
        } else {
            departmentPage = DrugRecordsDepartmentPage(context, container)
            container.addView(departmentPage!!.view)
            return departmentPage!!.view
        }
    }

    fun refreshHospitalPage() {
        hospitalPage?.initHospitalListAdapter()
        hospitalPage?.initHospitalListRecyclerView()
    }

    fun refreshDepartmentPage() {
        departmentPage?.initDepartmentListAdapter()
        departmentPage?.initDepartmentListRecyclerView()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}