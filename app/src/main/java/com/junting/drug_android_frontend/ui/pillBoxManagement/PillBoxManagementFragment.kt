package com.junting.drug_android_frontend.ui.pillBoxManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.junting.drug_android_frontend.databinding.FragmentPillBoxManagementBinding

class PillBoxManagementFragment : Fragment() {

    private var _binding: FragmentPillBoxManagementBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val pillBoxManagementViewModel =
            ViewModelProvider(this).get(PillBoxManagementViewModel::class.java)

        _binding = FragmentPillBoxManagementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPillBoxManagement
        pillBoxManagementViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}