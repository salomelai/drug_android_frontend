package com.junting.drug_android_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.junting.drug_android_frontend.databinding.ActivityMainBinding
import com.junting.drug_android_frontend.databinding.FontSizeDialogLayoutBinding
import com.junting.drug_android_frontend.libs.FontSizeManager
import com.junting.drug_android_frontend.libs.LanguageUtil
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var headerTextView: TextView
    private var idToken: String? = null
//    var receiveIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idToken = intent.getStringExtra("googleToken")

        if(idToken != null){
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
                supportActionBar!!.customView.findViewById<TextView>(R.id.action_bar_title).text =
                    d.label
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_outline_menu_24)
            }

//          登出
            navigationView = findViewById(R.id.drawer_nav)
            headerTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_sign_out)

            // 設置文字底線效果
            headerTextView.paintFlags = headerTextView.paintFlags

            // 設置點擊監聽器
            headerTextView.setOnClickListener {
                showLogoutConfirmationDialog()
            }



        initLanguageMenuItemTitle()

        binding.drawerNav.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_drug_interaction_lookup -> {
                    Log.d("drawerNav.", "nav_tools")
                    // 在這裡處理點擊事件
                    binding.drawer.closeDrawer(GravityCompat.START)
                    val intent = Intent(this, MedicineSearchActivity::class.java)
                    startActivity(intent)
                    true
                }

                    R.id.nav_language -> {
                        Log.d("drawerNav.", "nav_language")
                        // 在這裡處理點擊事件
                        //switchLanguage
                        //修改配置
                        LanguageUtil.settingLanguage(this, LanguageUtil.getInstance())
                        binding.drawer.closeDrawer(GravityCompat.START)
                        //activity銷毀重建
                        this.recreate()

                        true
                    }

                    R.id.nav_fontSize -> {
                        Log.d("drawerNav.", "nav_fontSize")
                        // 在這裡處理點擊事件
                        initFontSizeDialog()
                        binding.drawer.closeDrawer(GravityCompat.START)
                        true

                    }

                    else -> false
                }
            }

            val fragmentName = intent.getStringExtra("fragmentName")
            if (fragmentName == "TodayReminderFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_todayReminder)
            } else if (fragmentName == "DrugRecordsFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_drugRecords)
            } else if (fragmentName == "TakeRecordsFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_takeRecords)
            } else if (fragmentName == "PillBoxManagementFragment") {
                navController.popBackStack()
                navController.navigate(R.id.navigation_pillBoxManagement)
            }
        }else{
            startActivity(Intent(this@MainActivity, IntroductoryActivity::class.java))
            finish()
        }

    }
// out of onCreate
    private fun initFontSizeDialog() {
        val binding = FontSizeDialogLayoutBinding.inflate(layoutInflater)
        val fontSizeSeekBar = binding.seekbar
        fontSizeSeekBar.progress = FontSizeManager.getCurrentFontScaleIndex()

        // 設置滑桿的監聽器
        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // 根據進度值調整字體大小
                Log.d("fontSizeSeekBar", "fontScale: $FontSizeManager.getCurrentFontScaleIndex()")
                FontSizeManager.setCurrentFontScaleIndex(progress)
                setAppFontSize(FontSizeManager.getCurrentFontScale())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // 滑桿觸摸開始時的處理
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // 滑桿觸摸結束時的處理
            }
        })

        // 建立對話框
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.adjust_text_size))
            .setView(binding.root)
            .setPositiveButton(resources.getString(R.string.confirm)) { dialog, _ ->
                // 按下確定按鈕的處理
                recreate() // 重新啟動 Activity 以應用新的字體大小設定
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, _ ->
                // 按下取消按鈕的處理
                dialog.dismiss()
            }
            .show()
    }

    fun initLanguageMenuItemTitle() {
        val currentLocale: Locale = resources.configuration.locales.get(0)
        val language: String = currentLocale.language
        if (language == "en") {
            binding.drawerNav.menu.findItem(R.id.nav_language).title = "中文"
        } else if (language == "zh") {
            binding.drawerNav.menu.findItem(R.id.nav_language).title = "English"
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("TAG", "onRestoreInstanceState");
        activityReset();
    }

    private fun activityReset() {
        //同一个对象在同一个线程中获取的数据是一样的，因为每个线程都维护了一个ThreadLocalMap对象，key值是ThreadLocal
        val threadLocal: ThreadLocal<String> = LanguageUtil.getInstance() as ThreadLocal<String>
        val language: String? = threadLocal.get()
        Log.d("language: ", language.toString())
        if (language == "English") {
            binding.drawerNav.menu.findItem(R.id.nav_language).title = "中文"
        } else if (language == "Chinese") {
            binding.drawerNav.menu.findItem(R.id.nav_language).title = "English"
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
    private fun setAppFontSize(fontScale: Float) {
        val configuration = resources.configuration
        configuration.fontScale = fontScale
        val metrics = resources.displayMetrics
        resources.updateConfiguration(configuration, metrics)
    }

    fun setTodayReminderBadge(number: Int) {
        val navView: BottomNavigationView = binding.navView
        val badge = navView.getOrCreateBadge(R.id.navigation_todayReminder)
        badge.number = number
        badge.isVisible = true
    }

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.sign_out_confirmation))
            .setMessage(resources.getString(R.string.sign_out_text))
            .setPositiveButton(resources.getString(R.string.confirm)) { dialog, _ ->
                // 點擊確定按鈕時執行登出操作
                performLogout()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, _ ->
                // 點擊取消按鈕時關閉對話框
                dialog.dismiss()
            }
            .show()
    }
    private fun performLogout() {
        idToken = null
        startActivity(Intent(this@MainActivity, IntroductoryActivity::class.java))
        finish()
    }
}