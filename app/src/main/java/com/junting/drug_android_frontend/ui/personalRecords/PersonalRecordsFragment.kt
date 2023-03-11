package com.junting.drug_android_frontend.ui.personalRecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.junting.drug_android_frontend.databinding.FragmentPersonalRecordsBinding

class PersonalRecordsFragment : Fragment() {

    private var _binding: FragmentPersonalRecordsBinding? = null
    private var personalRecordsPagerAdapter: PersonalRecordsPagerAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonalRecordsBinding.inflate(inflater, container, false)
        setViewPager()
        return binding.root
    }

    private fun setViewPager() {
        personalRecordsPagerAdapter = PersonalRecordsPagerAdapter(this.requireContext())
        binding.personalRecordsViewPager.adapter = personalRecordsPagerAdapter
        binding.personalRecordsViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.personalRecordsTabLayout))
        binding.personalRecordsTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                binding.personalRecordsViewPager.currentItem = position
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