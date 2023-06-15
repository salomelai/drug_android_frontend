package com.junting.drug_android_frontend.ui.takeRecords

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.junting.drug_android_frontend.R


class TakeRecordsByTimePage(context: Context, container: ViewGroup) {

    val view: View
    private val context: Context
    private val container: ViewGroup
    private val progressBar: ProgressBar


    private val viewModel: TakeRecordsViewModel


    init {
        this.context = context
        this.container = container
        this.view =
            LayoutInflater.from(context).inflate(R.layout.take_records_time_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)

        this.viewModel = TakeRecordsViewModel()
        initViewModel()
    }
    private fun initViewModel() {
        this.viewModel.fetchRecords()
        this.viewModel.dateTimeSlotRecords.observe(context as AppCompatActivity, Observer {
                dateTimeSlotRecords ->
            Log.d("TakeRecordsByTimePage", "dateTimeSlotRecords: $dateTimeSlotRecords")
//            initExpandableList(medications)
            progressBar.visibility = View.GONE
        })

    }
//    private fun initExpandableList(medications: List<Medication>) {
//        expandableListAdapter = TakeRecordsByDrugExpandableListAdapter(context, medications)
//        expandableListView.setAdapter(expandableListAdapter)
//
//    }



}