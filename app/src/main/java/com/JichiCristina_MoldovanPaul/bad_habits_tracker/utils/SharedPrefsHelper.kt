package com.jichicristina_moldovanpaul.bad_habits_tracker.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("bad_habits_prefs", Context.MODE_PRIVATE)

    var username: String
        get() = prefs.getString("USERNAME", "") ?: ""
        set(value) = prefs.edit().putString("USERNAME", value).apply()

    var password: String
        get() = prefs.getString("PASSWORD", "") ?: ""
        set(value) = prefs.edit().putString("PASSWORD", value).apply()

    var motivationMsg: String
        get() = prefs.getString("MOTIVATION_MSG", "") ?: ""
        set(value) = prefs.edit().putString("MOTIVATION_MSG", value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean("IS_LOGGED_IN", false)
        set(value) = prefs.edit().putBoolean("IS_LOGGED_IN", value).apply()
}
