package com.junting.drug_android_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import com.junting.drug_android_frontend.databinding.ActivityInteractionSearchBinding

class InteractionSearchActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String?>
    private lateinit var binding: ActivityInteractionSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInteractionSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 隱藏標題
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = ArrayAdapter<String?>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.countries_array))
        binding.lvListView.adapter = adapter
        binding.lvListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String?
            Toast.makeText(this, item, Toast.LENGTH_SHORT).show()
        }
        binding.lvListView.emptyView = binding.tvEmpty

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val search:MenuItem? = menu?.findItem(R.id.nav_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}