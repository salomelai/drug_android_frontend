package com.junting.drug_android_frontend.ui.todayReminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.databinding.FragmentTodayReminderBinding

class TodayReminderFragment : Fragment() {

    private var _binding: FragmentTodayReminderBinding? = null
    private lateinit var viewAdapter: TodayReminderViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private  var viewModel: TodayReminderViewModel = TodayReminderViewModel()

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

//        initRecyclerView()
//        initRecyclerViewModel()
        val dataSet = listOf("Item 1", "Item 2", "Item 3")
        val mAdapter = TodayReminderViewAdapter2(dataSet)
        val mList = binding.list
        mList.layoutManager = LinearLayoutManager(requireContext())
        mList.adapter = mAdapter

        mList.behindSwipedItemBackgroundColor = R.color.md_theme_dark_primary
        mList.behindSwipedItemBackgroundSecondaryColor = R.color.md_theme_light_outline
        mList.behindSwipedItemIconDrawableId = R.drawable.ic_outline_notifications_24
        mList.behindSwipedItemIconSecondaryDrawableId  = R.drawable.ic_outline_notifications_off_24

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerViewModel() {
        viewModel.fetchRecords()
        viewModel.records.observe(context as AppCompatActivity, Observer {
            viewAdapter!!.notifyDataSetChanged()
//            progressBar.visibility = View.GONE
        })
    }

//    private fun initRecyclerView() {
//        viewManager = LinearLayoutManager(requireContext())
//        viewAdapter = TodayReminderViewAdapter(requireContext(), viewModel)
//        binding.recyclerView.apply {
//            layoutManager = viewManager
//            adapter = viewAdapter
//        }
//    }
}