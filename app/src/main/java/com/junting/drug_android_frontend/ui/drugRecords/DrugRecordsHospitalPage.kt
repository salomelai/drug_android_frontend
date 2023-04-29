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


class DrugRecordsHospitalPage(context: Context, container: ViewGroup) {

    val view: View
    private val context: Context
    private val container: ViewGroup

    private val progressBar: ProgressBar

    private val recyclerView: RecyclerView
    private val hospitalListRecyclerView: RecyclerView

    private var recyclerAdapter: DrugsRecordViewAdapter
    private var hostpitalListRecyclerAdapter: HospitalListViewAdapter

    private val viewModel: DrugRecordsViewModel
    private val hospitalListViewModel : HospitalListViewModel



    init {
        this.context = context
        this.container = container
        this.view = LayoutInflater.from(context).inflate(R.layout.drug_records_hospital_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)

        this.recyclerView = view.findViewById(R.id.recycler_view)
        this.hospitalListRecyclerView = view.findViewById(R.id.hospital_list_recycler_view)

        this.viewModel = DrugRecordsViewModel()
        this.hospitalListViewModel = HospitalListViewModel()

        this.recyclerAdapter = DrugsRecordViewAdapter(context, viewModel)
        this.hostpitalListRecyclerAdapter = HospitalListViewAdapter(context, hospitalListViewModel)

        this.initAdapter()
        this.initHospitalListAdapter()

        this.initRecyclerView()
        this.initHospitalListRecyclerView()

        this.initProgressBar()
    }

    private fun initHospitalListAdapter() {
        this.hospitalListViewModel.fetchRecords()
        this.hospitalListViewModel.hospitals.observe(context as AppCompatActivity, Observer {
            hostpitalListRecyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
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

    private fun initHospitalListRecyclerView() {
        hospitalListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hostpitalListRecyclerAdapter
        }
    }
    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }
}