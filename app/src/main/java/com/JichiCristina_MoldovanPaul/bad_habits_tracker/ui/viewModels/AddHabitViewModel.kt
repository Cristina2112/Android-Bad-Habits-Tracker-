package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.HabitDao
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.HabitEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddHabitViewModel(
    private val habitDao: HabitDao,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    fun saveHabit(habitName: String, selectedDateMillis: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            val userId = authDataStore.loggedInUserIdFlow.first()
            if (userId != -1) {
                val newHabit = HabitEntity(
                    habitName = habitName,
                    startDate = selectedDateMillis,
                    userId = userId
                )
                habitDao.insertHabit(newHabit)
                onComplete()
            }
        }
    }
}

class AddHabitViewModelFactory(
    private val habitDao: HabitDao,
    private val authDataStore: AuthDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddHabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddHabitViewModel(habitDao, authDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
