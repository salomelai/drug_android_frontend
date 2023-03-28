package com.junting.drug_android_frontend

import android.os.Bundle
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.junting.drug_android_frontend.databinding.ActivityEditDrugRecordBinding

class EditDrugRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDrugRecordBinding

    lateinit var groupList: MutableList<String>
    lateinit var childList: MutableList<String>
    lateinit var mobileCollection: Map<String, List<String>>
    lateinit var expandableListAdapter: ExpandableListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditDrugRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createGroupList()
        createCollection()

        expandableListAdapter = EditRrugExpandableListAdapter(this, groupList, mobileCollection)
        binding.expandableListInteraction.setAdapter(expandableListAdapter)
        binding.expandableListInteraction.setOnGroupExpandListener(object : OnGroupExpandListener {
            var lastExpandedPosition = -1
            override fun onGroupExpand(i: Int) {
                if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                    binding.expandableListInteraction.collapseGroup(lastExpandedPosition)
                }
                lastExpandedPosition = i
            }
        })
        binding.expandableListInteraction.setOnChildClickListener { expandableListView, view, i, i1, l ->
            val selected = expandableListAdapter.getChild(i, i1).toString()
            Toast.makeText(applicationContext, "Selected: $selected", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun createCollection() {
        val samsungModels = arrayOf(
            "Samsung Galaxy M21", "Samsung Galaxy F41",
            "Samsung Galaxy M51", "Samsung Galaxy A50s"
        )
        mobileCollection = HashMap()
        for (group in groupList) {
            loadChild(samsungModels)
            (mobileCollection as HashMap<String, List<String>>)[group] = childList
        }
    }

    private fun loadChild(mobileModels: Array<String>) {
        childList = ArrayList()
        for (model in mobileModels) {
            childList.add(model)
        }
    }

    private fun createGroupList() {
        groupList = ArrayList()
        groupList.add("Samsung")
    }
}