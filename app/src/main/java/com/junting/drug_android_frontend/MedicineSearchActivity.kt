package com.junting.drug_android_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.junting.drug_android_frontend.databinding.ActivityMedicineSearchBinding

class MedicineSearchActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String?>
    private var viewModel: MedicineSearchViewModel = MedicineSearchViewModel()
    private lateinit var binding: ActivityMedicineSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 隱藏標題
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initViewModel()

        binding.lvListView.emptyView = binding.tvEmpty
        // 設置搜尋監聽器
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 提交搜尋文字
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 搜尋文字改變時觸發
                adapter.filter.filter(newText)
                return true
            }
        })



    }

    private fun initViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchRecords()
        viewModel.medicines.observe(this, Observer {
            adapter = ArrayAdapter<String?>(this, android.R.layout.simple_list_item_1, it.map { medicine -> medicine.country })
            binding.lvListView.adapter = adapter
            binding.lvListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as String?
                Toast.makeText(this, item, Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility = View.GONE
        })
    }
}