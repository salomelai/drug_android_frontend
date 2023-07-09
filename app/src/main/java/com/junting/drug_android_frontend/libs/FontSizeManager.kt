package com.junting.drug_android_frontend.libs

object FontSizeManager {
    private val fontScales = listOf(0.9f, 1f, 1.1f, 1.2f, 1.3f)
    private var currentFontScaleIndex = 1 // 預設字體大小的索引

    fun getCurrentFontScale(): Float {
        return fontScales[currentFontScaleIndex]
    }

    fun getCurrentFontScaleIndex(): Int {
        return currentFontScaleIndex
    }
    fun setCurrentFontScaleIndex(index: Int) {
        currentFontScaleIndex = index
    }

    fun increaseFontScale() {
        if (currentFontScaleIndex < fontScales.size - 1) {
            currentFontScaleIndex++
        }
    }

    fun decreaseFontScale() {
        if (currentFontScaleIndex > 0) {
            currentFontScaleIndex--
        }
    }
}