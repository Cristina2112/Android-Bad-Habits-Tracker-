package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.UserDao
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val userDao: UserDao,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userDao.getUser(username, password)
            if (user != null) {
                authDataStore.saveUserId(user.id)
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Date incorecte sau cont inexistent")
            }
        }
    }

    fun register(username: String, password: String, motivation: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val newUser = UserEntity(username = username, password = password, motivationMsg = motivation)
            val newId = userDao.insertUser(newUser).toInt()
            authDataStore.saveUserId(newId)
            _authState.value = AuthState.Success
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

class AuthViewModelFactory(
    private val userDao: UserDao,
    private val authDataStore: AuthDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userDao, authDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
