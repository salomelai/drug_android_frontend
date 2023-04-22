package com.junting.drug_android_frontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.junting.drug_android_frontend.databinding.FragmentNotificationSettingBinding

class NotificationSettingFragment:Fragment() {

    private var _binding: FragmentNotificationSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 使用 ViewBinding 來設定 MyFragment 的佈局，並返回佈局的根視圖 View

        _binding = FragmentNotificationSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        // 初始化並設置 Switch
        binding.switchStatus.isChecked = true // 設定預設為開啟
        // 設定 Switch 的監聽事件
        binding.switchStatus.setOnCheckedChangeListener { _, isChecked ->
            // 根據 Switch 的狀態執行相應的操作
            if (isChecked) {
                // Switch 開啟時的操作
                Toast.makeText(requireContext(), "Switch 開啟", Toast.LENGTH_SHORT).show()
            } else {
                // Switch 關閉時的操作
                Toast.makeText(requireContext(), "Switch 關閉", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // 在這裡可以設定進入和退出動畫
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (enter) {
            // 進入動畫
            return AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        } else {
            // 退出動畫
            return AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 清除 ViewBinding 的引用，避免內存洩漏
        _binding = null
    }
}