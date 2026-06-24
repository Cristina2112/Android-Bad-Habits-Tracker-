package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.AuthState
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.AuthViewModel
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.AuthViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val authViewModel: AuthViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val dataStore = AuthDataStore(requireContext())
        AuthViewModelFactory(database.userDao(), dataStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Te rog completează datele!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.login(username, password)
        }

        tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Loading -> {
                            btnLogin.isEnabled = false
                        }
                        is AuthState.Success -> {
                            btnLogin.isEnabled = true
                            authViewModel.resetState()
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build()
                            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment, null, navOptions)
                        }
                        is AuthState.Error -> {
                            btnLogin.isEnabled = true
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            authViewModel.resetState()
                        }
                        is AuthState.Idle -> {
                            btnLogin.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
