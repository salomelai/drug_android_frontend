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

    private val drugRecordsByHospitalRecyclerView: RecyclerView
    private val hospitalListRecyclerView: RecyclerView

    private var drugRecordsByHospitalRecyclerAdapter: DrugRecordsViewAdapter
    private var hostpitalListRecyclerAdapter: HospitalListViewAdapter

    private val drugRecordsByHospitalViewModel: DrugRecordsViewModel
    private val hospitalListViewModel : HospitalListViewModel



    init {
        this.context = context
        this.container = container
        this.view = LayoutInflater.from(context).inflate(R.layout.drug_records_hospital_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)

        this.drugRecordsByHospitalRecyclerView = view.findViewById(R.id.recycler_view)
        this.hospitalListRecyclerView = view.findViewById(R.id.hospital_list_recycler_view)

        this.drugRecordsByHospitalViewModel = DrugRecordsViewModel()
        this.hospitalListViewModel = HospitalListViewModel()

        this.drugRecordsByHospitalRecyclerAdapter = DrugRecordsViewAdapter(context, drugRecordsByHospitalViewModel)
        this.hostpitalListRecyclerAdapter = HospitalListViewAdapter(context, hospitalListViewModel,this)

//        this.initAdapter()
        this.initHospitalListAdapter()

//        this.initRecyclerView()
        this.initHospitalListRecyclerView()

        this.initProgressBar()
    }

    fun initHospitalListAdapter() {
        progressBar.visibility = View.VISIBLE
        this.hospitalListViewModel.fetchRecords()
        this.hospitalListViewModel.hospitals.observe(context as AppCompatActivity, Observer {
            hostpitalListRecyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    fun initDrugRecordsByHospitalAdapter(hospitalName: String) {
        progressBar.visibility = View.VISIBLE
        this.drugRecordsByHospitalViewModel.fetchRecordsByHospital(hospitalName)
        this.drugRecordsByHospitalViewModel.records.observe(context as AppCompatActivity, Observer {
            drugRecordsByHospitalRecyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    private fun initProgressBar() {

    }

    fun initHospitalListRecyclerView() {
        drugRecordsByHospitalRecyclerView.visibility = View.GONE
        hospitalListRecyclerView.visibility = View.VISIBLE
        hospitalListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hostpitalListRecyclerAdapter
        }
    }
    fun initDrugRecordsByHospitalRecyclerView() {
        drugRecordsByHospitalRecyclerView.visibility = View.VISIBLE
        hospitalListRecyclerView.visibility = View.GONE
        drugRecordsByHospitalRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = drugRecordsByHospitalRecyclerAdapter
        }
    }
}