package com.junting.drug_android_frontend.ui.takeRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.junting.drug_android_frontend.R

class TakeRecordsPagerAdapter(context: Context): PagerAdapter() {

    private val context: Context
    private var takeRecordsByDrugPage: TakeRecordsByDrugPage? = null
    private var takeRecordsByTimePage: TakeRecordsByTimePage? = null
    private var takeRecordsChart: TakeRecordsChart? = null

    init {
        this.context = context
    }

    override fun getCount(): Int {
        return 2
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position == 0) {
            takeRecordsByDrugPage = TakeRecordsByDrugPage(context, container)
            container.addView(takeRecordsByDrugPage!!.view)
            return takeRecordsByDrugPage!!.view
        } else if (position == 1) {
            takeRecordsByTimePage = TakeRecordsByTimePage(context, container)
            container.addView(takeRecordsByTimePage!!.view)
            return takeRecordsByTimePage!!.view
        }
//        else {
//            val view: View =
//                LayoutInflater.from(context).inflate(R.layout.take_records_chart_tab, container, false)
//            container.addView(view)
//            return view
//        }
        // 虚拟返回值，因为在此分支没有实际返回对象
        return Unit

    }



    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}