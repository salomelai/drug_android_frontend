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

    private val drugRecordsByHospitalrecyclerView: RecyclerView
    private val hospitalListRecyclerView: RecyclerView

    private var drugRecordsByHospitalrecyclerAdapter: DrugRecordsViewAdapter
    private var hostpitalListRecyclerAdapter: HospitalListViewAdapter

    private val drugRecordsByHospitalviewModel: DrugRecordsViewModel
    private val hospitalListViewModel : HospitalListViewModel



    init {
        this.context = context
        this.container = container
        this.view = LayoutInflater.from(context).inflate(R.layout.drug_records_hospital_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)

        this.drugRecordsByHospitalrecyclerView = view.findViewById(R.id.recycler_view)
        this.hospitalListRecyclerView = view.findViewById(R.id.hospital_list_recycler_view)

        this.drugRecordsByHospitalviewModel = DrugRecordsViewModel()
        this.hospitalListViewModel = HospitalListViewModel()

        this.drugRecordsByHospitalrecyclerAdapter = DrugRecordsViewAdapter(context, drugRecordsByHospitalviewModel)
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
        this.drugRecordsByHospitalviewModel.fetchRecordsByHospital(hospitalName)
        this.drugRecordsByHospitalviewModel.records.observe(context as AppCompatActivity, Observer {
            drugRecordsByHospitalrecyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    private fun initProgressBar() {

    }

    fun initHospitalListRecyclerView() {
        drugRecordsByHospitalrecyclerView.visibility = View.GONE
        hospitalListRecyclerView.visibility = View.VISIBLE
        hospitalListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hostpitalListRecyclerAdapter
        }
    }
    fun initDrugRecordsByHospitalRecyclerView() {
        drugRecordsByHospitalrecyclerView.visibility = View.VISIBLE
        hospitalListRecyclerView.visibility = View.GONE
        drugRecordsByHospitalrecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = drugRecordsByHospitalrecyclerAdapter
        }
    }
}