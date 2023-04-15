package com.junting.drug_android_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.ActivityDrugInteractionBinding
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation

class DrugInteractionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrugInteractionBinding
    private lateinit var viewAdapter: DrugInteractionViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: DrugInteractionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugInteractionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initRecyclerViewModel()
        initRecyclerView()
        initButton()

        val drugbagInfo = intent.getSerializableExtra("drugbagInfo") as DrugbagInformation
        Log.d("drugbagInfo name", drugbagInfo.drug.name)
    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            startActivity(intent)
        }
        binding.btnConfirm.setOnClickListener{
            val intent = Intent(this, DrugRecordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
