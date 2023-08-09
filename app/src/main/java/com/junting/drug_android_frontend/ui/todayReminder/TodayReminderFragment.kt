package com.junting.drug_android_frontend.ui.todayReminder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.junting.drug_android_frontend.MainActivity
import com.junting.drug_android_frontend.OnDemandListActivity
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.databinding.FragmentTodayReminderBinding
import com.junting.drug_android_frontend.model.ResponseMessage
import com.junting.drug_android_frontend.model.take_record.TakeRecord
import com.junting.drug_android_frontend.model.today_reminder.TodayReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodayReminderFragment : Fragment() {

    private var _binding: FragmentTodayReminderBinding? = null
    private lateinit var viewAdapter: TodayReminderViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var viewModel: TodayReminderViewModel = TodayReminderViewModel()
    private val onItemSwipeListener = object : OnItemSwipeListener<TodayReminder> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: TodayReminder): Boolean {
            if (direction == OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT) {
                Log.d("onItemSwiped", "向左滑: $position, $item")

                val takeRecord = TakeRecord(
                    todayReminderId = item.id,
                    status = 2
                )
                viewModel.viewModelScope.launch {
                    val responseMessage = viewModel.processTakeRecord(takeRecord).await()
                    if (responseMessage != null) {
                        Toast.makeText(requireContext(), "已略過", Toast.LENGTH_SHORT).show()
                        // 成功處理 TakeRecord
                        viewModel.fetchTodayReminders()
                    } else {
                        // 處理失敗
                        Toast.makeText(requireContext(), "系統錯誤", Toast.LENGTH_SHORT).show()
                        // ... 處理失敗的邏輯 ...
                    }
                }

            } else if (direction == OnItemSwipeListener.SwipeDirection.LEFT_TO_RIGHT) {
                Log.d("onItemSwiped", "向右滑: $position, $item")

                val currentTime = Calendar.getInstance()
                val currentTimeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime.time)

                val takeRecord = TakeRecord(
                    todayReminderId = item.id,
                    status = 1,
                    dosage = item.dosage,
                    timeSlot = currentTimeString
                )
                viewModel.viewModelScope.launch {
                    val responseMessage = viewModel.processTakeRecord(takeRecord).await()
                    if (responseMessage != null) {
                        Toast.makeText(requireContext(), "服用成功", Toast.LENGTH_SHORT).show()
                        // 成功處理 TakeRecord
                        viewModel.fetchTodayReminders()
                    } else {
                        // 處理失敗
                        Toast.makeText(requireContext(), "系統錯誤", Toast.LENGTH_SHORT).show()
                        // ... 處理失敗的邏輯 ...
                    }
                }
            }
            // 處理項目被滑動的動作
            // 返回 false 表示滑動的項目應該從適配器的資料集中移除（預設行為）
            // 返回 true 表示停止滑動的項目自動從適配器的資料集中移除（在這種情況下，你需要自行手動更新資料集）
//            updateTodayReminderBadge(viewAdapter.itemCount)
            return false
        }
    }
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
        initFab()
        initCheckIcon()
        // 設定 onItemSwipeListener
        binding.list.swipeListener = onItemSwipeListener

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
        builder.setTitle(resources.getString(R.string.select_time))
        builder.setItems(timeRanges) { _, index ->
            val selectedTimeRange = timeRanges[index]
            binding.inputEditText.setText(selectedTimeRange)
            binding.inputLayout.isEndIconVisible = true
        }
        builder.create().show()
    }
    private fun initCheckIcon(){
        val inputLayout = binding.inputLayout

        val endIconView = inputLayout.findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)
        endIconView?.setOnClickListener {
            val selectedTimeRange = binding.inputEditText.text.toString()
            Log.d("SelectedTimeRange", selectedTimeRange)

            val firstTwoDigits = selectedTimeRange.substring(0, 2).trim().toInt()
            val currentTime = Calendar.getInstance()
            val currentTimeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime.time)

            val takeRecord = TakeRecord(
                batchTime = firstTwoDigits,
                status = 4,
                timeSlot = currentTimeString
            )
            Log.d("TakeRecord", takeRecord.toString())

            // 使用viewModelScope的IO上下文执行网络请求
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                val responseMessage = viewModel.processTakeRecord(takeRecord).await()
                val dialogMessage = when {
                    responseMessage?.success == true -> "$selectedTimeRange 區段之藥物服用成功"
                    responseMessage?.success == false && responseMessage.errorCode == 1001 -> "未找到 $selectedTimeRange 區段之藥物"
                    else -> "系統錯誤"
                }

                // 使用Dispatchers.Main上下文更新UI
                withContext(Dispatchers.Main) {
                    val builder = MaterialAlertDialogBuilder(requireContext())
                    builder.setTitle(resources.getString(R.string.hint_title))
                    builder.setMessage(dialogMessage)
                    builder.setPositiveButton(R.string.confirm) { _, _ ->
                        // Handle positive button click
                        binding.inputLayout.isEndIconVisible = false

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("fragmentName", "TodayReminderFragment")
                        startActivity(intent)
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchTodayReminders()
        viewModel.todayReminders.observe(context as AppCompatActivity, Observer {
            updateTodayReminderBadge(it.size)
            viewAdapter!!.update(it)
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = TodayReminderViewAdapter(requireContext())
        val apply = binding.list.apply {
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
    private fun initFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(context, OnDemandListActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
