package com.junting.drug_android_frontend

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.junting.drug_android_frontend.databinding.ActivityMainBinding
import com.junting.drug_android_frontend.ui.todayReminder.TodayReminderViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    var googleIdToken = intent.getStringExtra("googleToken")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (googleIdToken == null) {
            val intent = Intent(this, WelcomePageActivity::class.java)
            startActivity(intent)
        }else{
            val navView: BottomNavigationView = binding.navView
            // 設定顯示小紅點圖標
            navView.getOrCreateBadge(R.id.navigation_todayReminder).apply {
                number = 0
                isVisible = true
            }

            navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_todayReminder, R.id.navigation_drugRecords,
                    R.id.navigation_takeRecords, R.id.navigation_pillBoxManagement
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


            // customize the action bar, whatever appearance you want it to be in action_bar_view
            supportActionBar!!.setDisplayShowCustomEnabled(true)
            supportActionBar!!.setCustomView(R.layout.action_bar_view)

            // refresh the actionbar menu when change fragment
            navController.addOnDestinationChangedListener { _: NavController, d: NavDestination, _: Bundle? ->
                supportActionBar!!.customView.findViewById<TextView>(R.id.action_bar_title).text = d.label
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_outline_menu_24)
            }

            binding.drawerNav.menu.iterator().forEach { menuItem ->
                menuItem.setOnMenuItemClickListener { _ ->
                    // navigate to other Activity or do something else
                    binding.drawer.closeDrawer(GravityCompat.START)
                    true
                }
            }

            val fragmentName = intent.getStringExtra("fragmentName")
            if(fragmentName=="TodayReminderFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_todayReminder)
            }
            else if (fragmentName == "DrugRecordsFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_drugRecords)
            }
            else if(fragmentName == "TakeRecordsFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_takeRecords)
            }
            else if(fragmentName == "PillBoxManagementFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_pillBoxManagement)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawer.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    fun setTodayReminderBadge(number: Int) {
        val navView: BottomNavigationView = binding.navView
        val badge = navView.getOrCreateBadge(R.id.navigation_todayReminder)
        badge.number = number
        badge.isVisible = true
    }
}