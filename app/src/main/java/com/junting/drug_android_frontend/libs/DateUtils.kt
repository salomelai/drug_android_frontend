package com.junting.drug_android_frontend.libs

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.junting.drug_android_frontend.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object DateUtils {
    fun calculateRemainingDate(
        context: Context,
        startDate: String,
        timeSlots: List<String>,
        dosage: Int,
        stock: Int,
        left: Int
    ): String {
        val remainingDays: Double
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        // 獲取當前日期
        val currentDate = Date()
        val startDateDate = dateFormat.parse(startDate) // 將startDate從String轉換為Date

        // 判斷startDate與當前日期的關係
        val referenceDate = if (startDateDate == null || !startDateDate.after(currentDate)) currentDate else startDateDate

        Log.d(TAG, "calculateRemainingDate: $referenceDate")

        // 計算剩餘天數
        if (timeSlots.isEmpty()) {
            return context.getString(R.string.none)
        } else if (dosage == 0 || (stock - left) <= 0) {
            return dateFormat.format(referenceDate)
        } else {
            remainingDays = (stock - left).toDouble() / (dosage * timeSlots.size)
        }

        // 使用 Calendar 進行日期計算
        val calendar = Calendar.getInstance()
        calendar.time = referenceDate
        calendar.add(Calendar.DAY_OF_MONTH, remainingDays.toInt())

        // 格式化日期為字串
        val remainingDate = dateFormat.format(calendar.time)

        return remainingDate
    }

}
