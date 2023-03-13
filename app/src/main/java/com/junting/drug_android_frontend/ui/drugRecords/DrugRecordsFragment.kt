package com.junting.drug_android_frontend.ui.drugRecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.junting.drug_android_frontend.databinding.FragmentDrugRecordsBinding

class DrugRecordsFragment : Fragment() {

    private var _binding: FragmentDrugRecordsBinding? = null
    private var drugRecordsPagerAdapter: DrugRecordsPagerAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDrugRecordsBinding.inflate(inflater, container, false)
        setViewPager()
        return binding.root
    }

    private fun setViewPager() {
        drugRecordsPagerAdapter = DrugRecordsPagerAdapter(this.requireContext())
        binding.drugRecordsViewPager.adapter = drugRecordsPagerAdapter
        binding.drugRecordsViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.drugRecordsTabLayout))
        binding.drugRecordsTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                binding.drugRecordsViewPager.currentItem = position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}