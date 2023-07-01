package com.junting.drug_android_frontend

import android.content.Intent
import android.net.Uri
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

    private lateinit var drugbagInfo: DrugbagInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugInteractionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initRecyclerViewModel()
        initRecyclerView()
        initButton()

        if (intent.getSerializableExtra("drugbagInfo") != null) {
            drugbagInfo = intent.getSerializableExtra("drugbagInfo") as DrugbagInformation
            Log.d("DrugbagInfo obj", drugbagInfo.toString())
        }
    }

    private fun initButton() {
        binding.btnCall.setOnClickListener {
            val phone = drugbagInfo.hospital.phone
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            startActivity(intent)
        }
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, DrugRecordActivity::class.java)
            viewModel.drugInteractions.value?.let {
                intent.putExtra("drugInteractions", ArrayList(it))
            }
            intent.putExtra("drugbagInfo", drugbagInfo)
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
            val hasMajorInteraction = it.any { drugInteraction ->
                drugInteraction.degree == "Major"
            }

            if (hasMajorInteraction) {
                binding.tvMessage.text = "${drugbagInfo.drug.name}"+resources.getString(R.string.interaction_major_illustrate_first)+" ${it.size} "+resources.getString(R.string.interaction_major_illustrate_second)
                binding.btnConfirm.visibility = View.GONE
            } else {
                binding.tvMessage.text = "${drugbagInfo.drug.name}"+resources.getString(R.string.interaction_not_major_illustrate_first)+" ${it.size} "+resources.getString(R.string.interaction_not_major_illustrate_second)
            }

            binding.progressBar.visibility = View.GONE
        })
    }
}
