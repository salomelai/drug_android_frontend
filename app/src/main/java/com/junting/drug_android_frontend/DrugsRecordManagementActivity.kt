package com.junting.drug_android_frontend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.ActivityDrugsRecordManagementBinding
import com.junting.drug_android_frontend.model.Record
import com.junting.drug_android_frontend.services.DrugService
import kotlinx.coroutines.launch

class DrugsRecordManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrugsRecordManagementBinding
    private lateinit var viewAdapter: DrugsRecordManagementAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: DrugsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugsRecordManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerViewModel()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = DrugsRecordManagementAdapter(this, viewModel)
        binding.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@DrugsRecordManagementActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initRecyclerViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel = DrugsViewModel()
        viewModel.fetchRecords()
        viewModel.records.observe(this, Observer {
            viewAdapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        })
    }

}

class DrugsViewModel: ViewModel() {

    var records = MutableLiveData<List<Record>>()

    fun fetchRecords() {
        viewModelScope.launch {
            val drugService = DrugService.getInstance()
            try {
                records.value = drugService.getDrugs()
            } catch (e: Exception) {
                Log.d("DrugsViewModel", "fetch records failed")
                Log.e("DrugsViewModel", e.toString())
            }
        }
    }
}