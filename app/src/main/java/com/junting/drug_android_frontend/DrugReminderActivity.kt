package com.junting.drug_android_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder

class DrugReminderActivity : AppCompatActivity() {
    var todayReminder : TodayReminder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_reminder)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        todayReminder = intent.getSerializableExtra("todayReminder") as? TodayReminder
        if (todayReminder != null) {
            supportActionBar?.setTitle(todayReminder!!.drug.name)
        }
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
}