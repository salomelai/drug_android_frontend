package com.junting.drug_android_frontend.ui.drugRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.junting.drug_android_frontend.R

class DrugRecordsPagerAdapter(context: Context): PagerAdapter() {

    private val context: Context

    private lateinit var viewAdapter: DrugsRecordViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: DrugRecordsViewModel

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
                LayoutInflater.from(context).inflate(R.layout.drug_records_all_tab, container, false)
            initRecyclerViewModel(view)
            initRecyclerView(view)

            container.addView(view)
            return view
        } else if (position == 1) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.drug_records_hospital_tab, container, false)
            container.addView(view)
            return view
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.drug_records_department_tab, container, false)
            container.addView(view)
            return view
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun initRecyclerViewModel(view: View){
        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
        viewModel = DrugRecordsViewModel()
        viewModel.fetchRecords()
        viewModel.records.observe(context as AppCompatActivity, Observer {
            viewAdapter.notifyDataSetChanged()
            view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        })
    }
    fun initRecyclerView(view: View){
        viewManager = LinearLayoutManager(context)
        viewAdapter = DrugsRecordViewAdapter(context, viewModel)
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

}