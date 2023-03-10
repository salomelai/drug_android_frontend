package com.junting.drug_android_frontend.ui.personalRecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.junting.drug_android_frontend.databinding.FragmentPersonalRecordsBinding

class PersonalRecordsFragment : Fragment() {

    private var _binding: FragmentPersonalRecordsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val personalRecordsViewModel =
            ViewModelProvider(this).get(PersonalRecordsViewModel::class.java)

        _binding = FragmentPersonalRecordsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPersonalRecords
        personalRecordsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}