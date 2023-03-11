package com.junting.drug_android_frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomappbar.BottomAppBar
import com.junting.drug_android_frontend.databinding.ActivityMainBinding
import com.junting.drug_android_frontend.ui.personalRecords.PersonalRecordsFragment
import com.junting.drug_android_frontend.ui.pillBoxManagement.PillBoxManagementFragment
import com.junting.drug_android_frontend.ui.todayReminder.TodayReminderFragment
import com.junting.drug_android_frontend.ui.tools.ToolsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var personalRecordsFragment: PersonalRecordsFragment = PersonalRecordsFragment()
    private var pillBoxManagementFragment: PillBoxManagementFragment = PillBoxManagementFragment()
    private var toolsFragment: ToolsFragment = ToolsFragment()
    private var todayReminderFragment: TodayReminderFragment = TodayReminderFragment()
    private val fragmentManager = supportFragmentManager
    var active: Fragment = personalRecordsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager.beginTransaction().add(R.id.host_fragment, personalRecordsFragment, "personalRecords")
            .commit()
        fragmentManager.beginTransaction().add(R.id.host_fragment, pillBoxManagementFragment, "pillBoxManagement")
            .hide(pillBoxManagementFragment)
            .commit()
        fragmentManager.beginTransaction().add(R.id.host_fragment, toolsFragment, "tools")
            .hide(toolsFragment)
            .commit()
        fragmentManager.beginTransaction().add(R.id.host_fragment, todayReminderFragment, "todayReminder")
            .hide(todayReminderFragment)
            .commit()

        val bottomAppBar: BottomAppBar = binding.bottomAppBar

        binding.bottomAppBar.setOnMenuItemClickListener{ menuItemClick ->
            when (menuItemClick.itemId) {
                R.id.navigation_personalRecords-> true
                else -> true
            }
        }

        bottomAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            if (it.itemId == R.id.navigation_personalRecords) {
                beginTransactionTo(personalRecordsFragment)
            } else if (it.itemId == R.id.navigation_pillBoxManagement) {
                beginTransactionTo(pillBoxManagementFragment)
            } else if (it.itemId == R.id.navigation_tools) {
                beginTransactionTo(toolsFragment)
            } else if (it.itemId == R.id.navigation_todayReminder) {
                beginTransactionTo(todayReminderFragment)
            }
            return@OnMenuItemClickListener true
        })
    }

    private fun beginTransactionTo(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.hide(active).show(fragment).commit()
        active = fragment
    }
}