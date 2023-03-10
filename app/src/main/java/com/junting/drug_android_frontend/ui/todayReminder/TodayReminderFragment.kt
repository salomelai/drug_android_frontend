package com.junting.drug_android_frontend.ui.todayReminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.junting.drug_android_frontend.databinding.FragmentTodayReminderBinding

class TodayReminderFragment : Fragment() {

    private var _binding: FragmentTodayReminderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val todayReminderViewModel =
            ViewModelProvider(this).get(TodayReminderViewModel::class.java)

        _binding = FragmentTodayReminderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTodayReminder
        todayReminderViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}