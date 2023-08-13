package com.junting.drug_android_frontend.ui.drugRecords

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.junting.drug_android_frontend.DrugbagInfoActivity
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.PhotoTakeActivity
import com.junting.drug_android_frontend.libs.SharedPreferencesManager


class DrugRecordsAllPage(context: Context, container: ViewGroup) {

    val view: View
    private val context: Context
    private val container: ViewGroup

    private val progressBar: ProgressBar
    private val recyclerView: RecyclerView
    private var recyclerViewAdapter: DrugRecordsViewAdapter
    private var fab: ExtendedFloatingActionButton

    private val viewModel: DrugRecordsViewModel

    private val sharedPreferencesManager = SharedPreferencesManager(context)

    init {
        this.context = context
        this.container = container
        this.view = LayoutInflater.from(context).inflate(R.layout.drug_records_all_tab, container, false)
        this.progressBar = view.findViewById(R.id.progressBar)
        this.recyclerView = view.findViewById(R.id.recycler_view)
        this.fab=view.findViewById(R.id.fab)
        this.viewModel = DrugRecordsViewModel()
        this.recyclerViewAdapter = DrugRecordsViewAdapter(context, viewModel)
        this.initAdapter()
        this.initProgressBar()
        this.initFab()
        this.initRecyclerView()
    }

    private fun initAdapter() {
        this.viewModel.fetchRecordsByAll()
        this.viewModel.records.observe(context as AppCompatActivity, Observer {
            recyclerViewAdapter!!.notifyDataSetChanged()

            // Create a set to store drug names
            val drugNamesSet = mutableSetOf<String>()

            // Iterate through fetched records
            for (record in it) {
                // Add the drug name to the set
                drugNamesSet.add(record.drug.name)
            }
            Log.d("drugNamesSet", drugNamesSet.toString())
            sharedPreferencesManager.saveOrUpdateDrugNames(drugNamesSet)

            progressBar.visibility = View.GONE
        })
    }

    private fun initProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }
    }
    private fun initFab(){


        fab.setOnClickListener(View.OnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.window_question_title))
                .setMessage(context.getString(R.string.drug_record_question_message))
                .setPositiveButton(context.getString(R.string.manual)) { dialogInterface, i ->
                    startActivity(context, Intent(context, DrugbagInfoActivity::class.java), null)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(context.getString(R.string.take_a_photo)) { dialogInterface, i ->
                    startActivity(context, Intent(context, PhotoTakeActivity::class.java), null)
                }
            builder.create()
            builder.show()
        })
    }


}