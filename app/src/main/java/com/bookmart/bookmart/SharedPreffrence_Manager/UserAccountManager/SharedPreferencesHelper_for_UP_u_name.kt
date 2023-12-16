package com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper_for_UP_u_name {
    private const val PREFS_NAME = "MyAppPreferences"
    private const val USERNAME_KEY = "username"

    fun saveUsername(context: Context, username: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(USERNAME_KEY, username)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(USERNAME_KEY, null)
    }
}
