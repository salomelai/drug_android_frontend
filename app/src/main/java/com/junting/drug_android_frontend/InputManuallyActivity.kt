package com.junting.drug_android_frontend

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.junting.drug_android_frontend.databinding.ActivityInputManuallyBinding

class InputManuallyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputManuallyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputManuallyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val frequencyOption = arrayOf(1, 2, 3, 4, 5)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencyOption)
        binding.actvFrequency.setAdapter(adapter)
    }
}