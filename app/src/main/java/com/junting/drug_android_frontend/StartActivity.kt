package com.junting.drug_android_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initView()
    }
    private fun initView() {
        val sp = getSharedPreferences("name", MODE_PRIVATE)
        val isValue = sp.getBoolean("ok", true)

        if (isValue) {
            val editor = sp.edit()
            editor.putBoolean("ok", false)
            editor.apply()
            startActivity(Intent(this@StartActivity, IntroductoryActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            finish()
        }
    }


}