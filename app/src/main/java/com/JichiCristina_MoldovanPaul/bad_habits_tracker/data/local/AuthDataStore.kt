package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(private val context: Context) {

    companion object {
        private val LOGGED_IN_USER_ID = intPreferencesKey("logged_in_user_id")
    }

    val loggedInUserIdFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[LOGGED_IN_USER_ID] ?: -1
        }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_USER_ID] = userId
        }
    }

    suspend fun clearUserId() {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_USER_ID] = -1
        }
    }
}
