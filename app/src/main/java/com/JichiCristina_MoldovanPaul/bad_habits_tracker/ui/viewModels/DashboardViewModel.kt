package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.HabitDao
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.HabitEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(
    private val habitDao: HabitDao,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    // Preia viciile reactiv în funcție de userul logat
    val habitsFlow: StateFlow<List<HabitEntity>> = authDataStore.loggedInUserIdFlow
        .flatMapLatest { userId ->
            habitDao.getHabitsForUser(userId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun logout() {
        viewModelScope.launch {
            authDataStore.clearUserId()
        }
    }
}

class DashboardViewModelFactory(
    private val habitDao: HabitDao,
    private val authDataStore: AuthDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(habitDao, authDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
