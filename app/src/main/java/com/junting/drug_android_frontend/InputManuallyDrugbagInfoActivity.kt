package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.junting.drug_android_frontend.databinding.ActivityInputManuallyBinding
import com.junting.drug_android_frontend.model.drugbag_info.Drug
import com.junting.drug_android_frontend.model.drugbag_info.DrugbagInformation

class InputManuallyDrugbagInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputManuallyBinding
    private lateinit var viewModel: DrugbagInfoViewModel
    private var checkBoxes: Array<CheckBox> = arrayOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputManuallyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkBoxes = arrayOf<CheckBox>(
            binding.cbAfterMeal,
            binding.cbAfterMeal,
            binding.cbWithFood,
            binding.cbBeforeSleep
        )

        initOndemandCheckbox()
        initFrequencyDropdown()
        initTimingsCheckbox()
        initDosageDropdown()
        initButton()

    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            super.onBackPressed()
        }
        binding.btnConfirm.setOnClickListener{
            val drugbagInfo = DrugbagInformation(
                id = 0,
                drug = Drug(
                    id = 0,
                    name = binding.tilDrugName.editText?.text.toString(),
                    indications = binding.tilIndication.editText?.text.toString(),
                    sideEffect = binding.tilSideEffect.editText?.text.toString(),
                    appearance = binding.tilAppearance.editText?.text.toString()
                ),
                hospitalName = binding.tilHospitalName.editText?.text.toString(),
                hospitalDepartment = binding.tilDepartment.editText?.text.toString(),
                onDemand = binding.cbOnDemand.isChecked,
                frequency = binding.actvFrequency.text.toString().toInt(),
                timings = checkBoxes.filter { it.isChecked }.map { checkBoxes.indexOf(it) },
                dosage = binding.actvDosage.text.toString().toInt(),
                stock = binding.tilStock.editText?.text.toString().toInt()
            )
            viewModel.sendDrugbagInfo(drugbagInfo)
        }
    }

    private fun initDosageDropdown() {
        val dosageOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dosageOption)
        binding.actvDosage.setAdapter(adapter)


        binding.actvDosage.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "您選擇了：$selectedItem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initOndemandCheckbox() {
        binding.cbOnDemand.setOnCheckedChangeListener { _, isChecked ->
            binding.tilFrequency.visibility = if (isChecked) View.GONE else View.VISIBLE
            binding.timingsOuterContainer.visibility = if (isChecked) View.GONE else View.VISIBLE
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

    private fun initFrequencyDropdown() {
        val frequencyOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencyOption)
        binding.actvFrequency.setAdapter(adapter)


        binding.actvFrequency.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "您選擇了：$selectedItem", Toast.LENGTH_SHORT).show()
        }
    }

}