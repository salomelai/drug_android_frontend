package com.junting.drug_android_frontend

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.junting.drug_android_frontend.databinding.ActivityDrugbagInfoBinding

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
        if(intent.getStringExtra("UglyText")?.isNotEmpty() == true){
            supportActionBar?.setTitle("修改藥袋資訊")
            initDrugbagInfoViewModel()
        }else{
            supportActionBar?.setTitle("新增藥袋資訊")
        }

        initOndemandCheckbox()
        initTimingsCheckbox()
        initButton()

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

    private fun initDrugbagInfoViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchDrugbagInfo()
        viewModel.drugbagInfo.observe(this, Observer {
//            binding.tilDrugName.editText?.setText(it.drug.name)
            binding.tilHospitalName.editText?.setText(it.hospitalName)
            binding.tilDepartment.editText?.setText(it.hospitalDepartment)
            binding.tilIndication.editText?.setText(it.drug.indications)
            binding.tilSideEffect.editText?.setText(it.drug.sideEffect)
            binding.tilAppearance.editText?.setText(it.drug.appearance)
            binding.cbOnDemand.isChecked = it.onDemand
            initFrequencyDropdown(it.frequency)
            for(i in it.timings){
                checkBoxes[i].isChecked = true
            }
            initDosageDropdown(it.dosage)
            binding.tilStock.editText?.setText(it.stock.toString())

            binding.progressBar.visibility = View.GONE
        })

    }


    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentName", "DrugRecordsFragment")
            startActivity(intent)
        }
        binding.btnConfirm.setOnClickListener{
            val intent = Intent(this, DrugInteractionActivity::class.java)
            viewModel.drugbagInfo.value?.let {
                intent.putExtra("drugbagInfo", it)
            }
            startActivity(intent)
        }
    }

    private fun initDosageDropdown(defaultValue: Int) {
        binding.actvDosage.setText(defaultValue.toString())
        val dosageOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, dosageOption)
        binding.actvDosage.setAdapter(adapter)


        binding.actvDosage.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "您選擇了：$selectedItem", Toast.LENGTH_SHORT).show()
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
            binding.cbAfterMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbAfterMeal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbWithFood.isEnabled = !isChecked
        }

        binding.cbWithFood.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.cbBeforeMeal.isEnabled = !isChecked
            binding.cbAfterMeal.isEnabled = !isChecked
        }
    }

    private fun initFrequencyDropdown(defaultValue : Int) {
        binding.actvFrequency.setText(defaultValue.toString())
        val frequencyOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, frequencyOption)
        binding.actvFrequency.setAdapter(adapter)

        binding.actvFrequency.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "您選擇了：$selectedItem", Toast.LENGTH_SHORT).show()
        }
    }

}
