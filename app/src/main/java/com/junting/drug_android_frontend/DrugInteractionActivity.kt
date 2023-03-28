package com.junting.drug_android_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.ActivityDrugInteractionBinding

class DrugInteractionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrugInteractionBinding
    private lateinit var viewAdapter: DrugInteractionViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: DrugInteractionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugInteractionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerViewModel()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = DrugInteractionViewAdapter(this, viewModel)
        binding.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun initRecyclerViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel = DrugInteractionViewModel()
        viewModel.fetchDrugInteraction()
        viewModel.drugInteractions.observe(this, Observer {
            viewAdapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        })
    }
}
