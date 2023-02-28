package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.databinding.ActivityDrugsRecordManagementBinding
import com.junting.drug_android_frontend.model.Drug
import com.junting.drug_android_frontend.services.DrugService
import okhttp3.Response


class DrugsRecordManagementActivity : AppCompatActivity(), DrugsRecordManagementAdapter.IItemClickListener {

    private lateinit var binding: ActivityDrugsRecordManagementBinding
    private lateinit var viewAdapter: DrugsRecordManagementAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_drugs_record_management)
        binding = ActivityDrugsRecordManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        getDrugsData()
    }

    private fun initView() {
        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 DrugsRecordManagementAdapter，稍後再定義 DrugsRecordManagementAdapter 這個類別
        viewAdapter = DrugsRecordManagementAdapter(this)

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recyclerView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
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

    private fun getDrugsData() {
        //顯示忙碌圈圈
        binding.progressBar.visibility = View.VISIBLE

        val drugService = DrugService.getInstance()
//        val drugInfo = drugService.getDrugs()
        val drugInfo = listOf<Drug>(
            Drug(id="1", appearance = "", dosage = 1, drug_name = "apple", start_date = "", end_date = "", hospital_department = "", indications = "", interacting_drugs = listOf(), side_effect = "", stock = 10, time_slot = listOf("")),
            Drug(id="2", appearance = "", dosage = 2, drug_name = "banana", start_date = "", end_date = "", hospital_department = "", indications = "", interacting_drugs = listOf(), side_effect = "", stock = 12, time_slot = listOf("")),
            Drug(id="3", appearance = "", dosage = 1, drug_name = "orange", start_date = "", end_date = "", hospital_department = "", indications = "", interacting_drugs = listOf(), side_effect = "", stock = 14, time_slot = listOf("")),
            Drug(id="4", appearance = "", dosage = 3, drug_name = "watermelon", start_date = "", end_date = "", hospital_department = "", indications = "", interacting_drugs = listOf(), side_effect = "", stock = 16, time_slot = listOf("")),
        )

        runOnUiThread {
            //將下載資料，指定給 DrugsRecordManagementAdapter
            viewAdapter.drugsList = drugInfo

            //關閉忙碌圈圈
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onItemClickListener(data: Drug) {
        Toast.makeText(this,"You clicked ${data.drug_name}", Toast.LENGTH_SHORT).show()
    }


}