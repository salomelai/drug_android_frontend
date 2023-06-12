package com.junting.drug_android_frontend.ui.todayReminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.junting.drug_android_frontend.MainActivity
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.databinding.FragmentTodayReminderBinding

class TodayReminderFragment : Fragment() {

    private var _binding: FragmentTodayReminderBinding? = null
    private lateinit var viewAdapter: TodayReminderViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var viewModel: TodayReminderViewModel = TodayReminderViewModel()

    private val timeRanges = generateTimeRanges()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentTodayReminderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecyclerView()
        initRecyclerViewModel()
        initChooseTime()

        return root
    }

    private fun generateTimeRanges(): Array<String> {
        val timeRanges = mutableListOf<String>()

        for (hour in 0..23) {
            val timeRange = String.format("%02d:00~%02d:59", hour, hour)
            timeRanges.add(timeRange)
        }

        return timeRanges.toTypedArray()
    }

    private fun initChooseTime() {
        binding.inputLayout.isEndIconVisible = false
        binding.inputEditText.setOnClickListener {
            showTimeRangeDialog()
        }
        binding.inputLayout.setEndIconOnClickListener {
            val selectedTimeRange = binding.inputEditText.text.toString()
            Log.d("SelectedTimeRange", selectedTimeRange)

        }
    }

    private fun showTimeRangeDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("選擇時段")
        builder.setItems(timeRanges) { _, index ->
            val selectedTimeRange = timeRanges[index]
            binding.inputEditText.setText(selectedTimeRange)
            binding.inputLayout.isEndIconVisible = true
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchRecords()
        viewModel.records.observe(context as AppCompatActivity, Observer {
            viewAdapter!!.update(it)
            updateTodayReminderBadge(it.size)
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = TodayReminderViewAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = viewAdapter
            behindSwipedItemBackgroundColor =
                ContextCompat.getColor(requireContext(), R.color.md_theme_light_outline)
            behindSwipedItemBackgroundSecondaryColor =
                ContextCompat.getColor(requireContext(), R.color.md_theme_dark_primary)
            behindSwipedItemIconDrawableId = R.drawable.ic_baseline_cancel_24
            behindSwipedItemIconSecondaryDrawableId = R.drawable.ic_baseline_check_circle_24
            behindSwipedItemIconMargin = 60.0f
        }
    }

    fun updateTodayReminderBadge(number: Int) {
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.setTodayReminderBadge(number) // 設定小紅點圖標數字
    }
}
