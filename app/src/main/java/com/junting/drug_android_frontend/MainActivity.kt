package com.junting.drug_android_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btn_camera_onclick(view: View) {
        Toast.makeText(this,"測試", Toast.LENGTH_SHORT).show()
    }
}