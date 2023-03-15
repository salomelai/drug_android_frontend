package com.junting.drug_android_frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.junting.drug_android_frontend.databinding.ActivityTakePhotoBinding

class TakePhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTakePhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(com.junting.drug_android_frontend.R.layout.action_bar_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}
