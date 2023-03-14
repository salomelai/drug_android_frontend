package com.junting.drug_android_frontend.ui.drugRecords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.R

class DrugRecordsAllPage(context: Context, container: ViewGroup) {

    val view: View
    private val context: Context
    private val container: ViewGroup

    private val progressBar: ProgressBar
    private val recyclerView: RecyclerView
    private var recyclerAdapter: DrugsRecordViewAdapter

    private val viewModel: DrugRecordsViewModel

    init {
        this.context = context
        this.container = container
        this.view = LayoutInflater.from(context).inflate(R.layout.drug_records_all_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)
        this.recyclerView = view.findViewById(R.id.recycler_view)
        this.viewModel = DrugRecordsViewModel()
        this.recyclerAdapter = DrugsRecordViewAdapter(context, viewModel)
        this.initAdapter()
        this.initProgressBar()
        this.initRecyclerView()
    }

    private fun initAdapter() {
        this.viewModel.fetchRecords()
        this.viewModel.records.observe(context as AppCompatActivity, Observer {
            recyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    private fun initProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }
}