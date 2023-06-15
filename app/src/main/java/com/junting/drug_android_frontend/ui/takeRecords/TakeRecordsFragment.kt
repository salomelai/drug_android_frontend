package com.junting.drug_android_frontend.ui.takeRecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.junting.drug_android_frontend.databinding.FragmentTakeRecordsBinding
import com.junting.drug_android_frontend.ui.drugRecords.DrugRecordsPagerAdapter

class TakeRecordsFragment: Fragment() {
    private var _binding: FragmentTakeRecordsBinding? = null
    private val binding get() = _binding!!
    private var takeRecordsPagerAdapter: TakeRecordsPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTakeRecordsBinding.inflate(inflater, container, false)
        setViewPager()
        return binding.root
    }

    private fun setViewPager() {
        takeRecordsPagerAdapter = TakeRecordsPagerAdapter(this.requireContext())
        binding.takeRecordsViewPager.adapter = takeRecordsPagerAdapter
        binding.takeRecordsViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.takeRecordsTabLayout
            )
        )
        binding.takeRecordsTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { updatePosition(tab) }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) { updatePosition(tab) }
            fun updatePosition(tab: TabLayout.Tab) {
                binding.takeRecordsViewPager.currentItem = tab.position

            }
        })

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}