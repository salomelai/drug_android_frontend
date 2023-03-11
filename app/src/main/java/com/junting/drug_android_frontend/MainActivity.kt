package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.junting.drug_android_frontend.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        // 設定顯示小紅點圖標
        navView.getOrCreateBadge(R.id.navigation_todayReminder).apply {
            number = 1
            isVisible = true
        }

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_todayReminder, R.id.navigation_personalRecords, R.id.navigation_takeRecords,
                R.id.navigation_pillBoxManagement, R.id.navigation_tools
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // customize the action bar, whatever appearance you want it to be in action_bar_view
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar_view)

        // refresh the actionbar menu when change fragment
        navController.addOnDestinationChangedListener { _: NavController, _: NavDestination, _: Bundle? ->
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // only records fragment should display action bar menu
        if (navController.currentDestination!!.id == R.id.navigation_personalRecords
            || navController.currentDestination!!.id == R.id.navigation_takeRecords) {
            menuInflater.inflate(R.menu.action_bar_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.take_records_option) {
            navController.navigate(R.id.navigation_takeRecords)
            true
        } else if (id == R.id.personal_records_option) {
            navController.navigate(R.id.navigation_personalRecords)
            true
        } else super.onOptionsItemSelected(item)
    }
}