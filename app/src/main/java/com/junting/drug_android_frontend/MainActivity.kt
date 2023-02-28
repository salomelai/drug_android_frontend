package com.junting.drug_android_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btn_camera_onclick(view: View) {
        startActivity(Intent(this, TakePhotoActivity::class.java))
    }

    fun btn_drugs_record_management_onclick(view: View) {
        startActivity(Intent(this, DrugsRecordManagementActivity::class.java))
    }
}