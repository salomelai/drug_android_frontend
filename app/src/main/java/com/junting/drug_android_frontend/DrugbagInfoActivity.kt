package com.junting.drug_android_frontend

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.junting.drug_android_frontend.databinding.ActivityDrugbagInfoBinding
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation
//import com.junting.drug_android_frontend.R

class DrugbagInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrugbagInfoBinding
    private var viewModel: DrugbagInfoViewModel = DrugbagInfoViewModel()
    private var checkBoxes: Array<CheckBox> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugbagInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        checkBoxes = arrayOf<CheckBox>(
            binding.cbAfterMeal,
            binding.cbAfterMeal,
            binding.cbWithFood,
            binding.cbBeforeSleep
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.getStringExtra("UglyText")?.isNotEmpty() == true) {
            supportActionBar?.setTitle(resources.getString(R.string.modify_drugbag_info))
            initDrugbagInfoViewModel()
        } else {
            supportActionBar?.setTitle(resources.getString(R.string.add_drugbag_info))
            unserInputDrugbagInfo()
        }

        initOndemandCheckbox()
        initTimingsCheckbox()
        initButton()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun unserInputDrugbagInfo(){
        viewModel.drugbagInfo.value = DrugbagInformation()
        initFrequencyDropdown(false)
        initDosageDropdown(false)
    }

    private fun initDrugbagInfoViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchDrugbagInfo()
        viewModel.drugbagInfo.observe(this, Observer {

            initFrequencyDropdown(true,it.frequency)
            for (i in it.timings) {
                checkBoxes[i].isChecked = true
            }
            initDosageDropdown(true,it.dosage)

            binding.progressBar.visibility = View.GONE
        })

    }


    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            startActivity(intent)
        }
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, DrugInteractionActivity::class.java)
            viewModel.drugbagInfo.value?.let {
                intent.putExtra("drugbagInfo", it)
            }
            startActivity(intent)
        }
    }

    private fun initDosageDropdown(hasDefault: Boolean, defaultValue: Int=0) {
        if (hasDefault) {
            binding.actvDosage.setText(defaultValue.toString())
        }
        val dosageOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, dosageOption)
        binding.actvDosage.setAdapter(adapter)


        binding.actvDosage.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this, resources.getString(R.string.select)+"：$selectedItem", Toast.LENGTH_SHORT).show()
                viewModel.drugbagInfo.value?.let {
                    it.dosage = selectedItem.toInt()
                }
            }
    }

    private fun initOndemandCheckbox() {
        binding.cbOnDemand.setOnCheckedChangeListener { _, isChecked ->
            binding.tilFrequency.visibility = if (isChecked) View.GONE else View.VISIBLE
            binding.llTimings.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

    }

    private fun initTimingsCheckbox() {
        binding.cbBeforeMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.drugbagInfo.value?.let {
                if (isChecked) {
                    // 新增 0 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(0) }
                } else {
                    // 移除 timings 列表中的 0
                    it.timings = it.timings.toMutableSet().apply { remove(0) }
                }
            }
            binding.cbAfterMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbAfterMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.drugbagInfo.value?.let {
                if (isChecked) {
                    // 新增 1 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(1) }
                } else {
                    // 移除 timings 列表中的 1
                    it.timings = it.timings.toMutableSet().apply { remove(1) }
                }
            }
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbWithFood.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.drugbagInfo.value?.let {
                if (isChecked) {
                    // 新增 2 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(2) }
                } else {
                    // 移除 timings 列表中的 2
                    it.timings = it.timings.toMutableSet().apply { remove(2) }
                }
            }
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbAfterMeal.isEnabled = !isChecked
        }
        binding.cbBeforeSleep.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.drugbagInfo.value?.let {
                if (isChecked) {
                    // 新增 3 到 timings 列表
                    it.timings = it.timings.toMutableSet().apply { add(3) }
                } else {
                    // 移除 timings 列表中的 3
                    it.timings = it.timings.toMutableSet().apply { remove(3) }
                }
            }
        }
    }


    private fun initFrequencyDropdown(hasDefault:Boolean,defaultValue: Int=1) {
        if(hasDefault){
            binding.actvFrequency.setText(defaultValue.toString())
        }
        val frequencyOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, frequencyOption)
        binding.actvFrequency.setAdapter(adapter)

        binding.actvFrequency.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this, resources.getString(R.string.select)+"：$selectedItem", Toast.LENGTH_SHORT).show()
                viewModel.drugbagInfo.value?.let {
                    it.frequency = selectedItem.toInt()
                }
            }
    }

}
