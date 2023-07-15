package com.junting.drug_android_frontend.libs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun saveGoogleIdToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("GOOGLE_ID_TOKEN", token)
        editor.apply()
    }

    fun getGoogleIdToken(): String? {
        return sharedPreferences.getString("GOOGLE_ID_TOKEN", null)
    }

    fun clearGoogleIdToken() {
        val editor = sharedPreferences.edit()
        editor.remove("GOOGLE_ID_TOKEN")
        editor.apply()
    }
}