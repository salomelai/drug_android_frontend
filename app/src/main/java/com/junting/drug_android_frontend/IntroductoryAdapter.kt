package com.junting.drug_android_frontend

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class IntroductoryAdapter(private val list: List<View>) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = list[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(list[position])
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

}