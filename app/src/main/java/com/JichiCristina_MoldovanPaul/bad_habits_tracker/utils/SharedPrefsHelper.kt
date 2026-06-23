package com.jichicristina_moldovanpaul.bad_habits_tracker.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("bad_habits_prefs", Context.MODE_PRIVATE)

    var loggedInUserId: Int
        get() = prefs.getInt("LOGGED_IN_USER_ID", -1)
        set(value) = prefs.edit().putInt("LOGGED_IN_USER_ID", value).apply()

    var isLoggedIn: Boolean
        get() = loggedInUserId != -1
        set(value) {
            if (!value) {
                loggedInUserId = -1
            }
        }
}
