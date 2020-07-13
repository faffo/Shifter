package com.faffo.shifter.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager

object Helpers {
    fun checkLogin(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean("isLoggedIn", false)
    }

    fun setLogin(context: Context, user: String, pw: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("isLoggedIn", true)
            .apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user", user)
            .apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("password", pw)
            .apply()
    }

    fun deleteLogin(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean("isLoggedIn", false).apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user", null)
            .apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("password", null)
            .apply()
    }
}