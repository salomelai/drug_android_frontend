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


class DrugRecordsDepartmentPage(context: Context, container: ViewGroup) {

    val view: View
    private val context: Context
    private val container: ViewGroup

    private val progressBar: ProgressBar

    private val drugRecordsByDepartmentRecyclerView: RecyclerView
    private val departmentListRecyclerView: RecyclerView

    private var drugRecordsByDepartmentRecyclerAdapter: DrugRecordsViewAdapter
    private var departmentListRecyclerAdapter: DepartmentListViewAdapter

    private val drugRecordsByDepartmentViewModel: DrugRecordsViewModel
    private val departmentListViewModel : DepartmentListViewModel



    init {
        this.context = context
        this.container = container
        this.view = LayoutInflater.from(context).inflate(R.layout.drug_records_department_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)

        this.drugRecordsByDepartmentRecyclerView = view.findViewById(R.id.recycler_view)
        this.departmentListRecyclerView = view.findViewById(R.id.department_list_recycler_view)

        this.drugRecordsByDepartmentViewModel = DrugRecordsViewModel()
        this.departmentListViewModel = DepartmentListViewModel()

        this.drugRecordsByDepartmentRecyclerAdapter = DrugRecordsViewAdapter(context, drugRecordsByDepartmentViewModel)
        this.departmentListRecyclerAdapter = DepartmentListViewAdapter(context, departmentListViewModel,this)

//        this.initAdapter()
        this.initDepartmentListAdapter()

//        this.initRecyclerView()
        this.initDepartmentListRecyclerView()

        this.initProgressBar()
    }

    fun initDepartmentListAdapter() {
        progressBar.visibility = View.VISIBLE
        this.departmentListViewModel.fetchRecords()
        this.departmentListViewModel.departments.observe(context as AppCompatActivity, Observer {
            departmentListRecyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    fun initDrugRecordsByDepartmentAdapter(department: String) {
        progressBar.visibility = View.VISIBLE
        this.drugRecordsByDepartmentViewModel.fetchRecordsByDepartment(department)
        this.drugRecordsByDepartmentViewModel.records.observe(context as AppCompatActivity, Observer {
            drugRecordsByDepartmentRecyclerAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    private fun initProgressBar() {

    }

    fun initDepartmentListRecyclerView() {
        drugRecordsByDepartmentRecyclerView.visibility = View.GONE
        departmentListRecyclerView.visibility = View.VISIBLE
        departmentListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = departmentListRecyclerAdapter
        }
    }
    fun initDrugRecordsByDepartmentRecyclerView() {
        drugRecordsByDepartmentRecyclerView.visibility = View.VISIBLE
        departmentListRecyclerView.visibility = View.GONE
        drugRecordsByDepartmentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = drugRecordsByDepartmentRecyclerAdapter
        }
    }
}