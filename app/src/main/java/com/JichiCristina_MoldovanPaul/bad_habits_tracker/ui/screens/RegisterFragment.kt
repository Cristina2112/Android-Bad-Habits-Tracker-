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

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val authViewModel: AuthViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val dataStore = AuthDataStore(requireContext())
        AuthViewModelFactory(database.userDao(), dataStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsernameReg = view.findViewById<EditText>(R.id.etUsernameReg)
        val etPasswordReg = view.findViewById<EditText>(R.id.etPasswordReg)
        val etMotivation = view.findViewById<EditText>(R.id.etMotivation)
        val btnCreateAccount = view.findViewById<Button>(R.id.btnCreateAccount)
        val tvBackToLogin = view.findViewById<TextView>(R.id.tvBackToLogin)

        btnCreateAccount.setOnClickListener {
            val username = etUsernameReg.text.toString()
            val password = etPasswordReg.text.toString()
            val motivation = etMotivation.text.toString()

            if (username.isBlank() || password.isBlank() || motivation.isBlank()) {
                Toast.makeText(requireContext(), "Toate câmpurile sunt obligatorii!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.register(username, password, motivation)
        }

        tvBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Loading -> {
                            btnCreateAccount.isEnabled = false
                        }
                        is AuthState.Success -> {
                            btnCreateAccount.isEnabled = true
                            Toast.makeText(requireContext(), "Cont creat cu succes!", Toast.LENGTH_SHORT).show()
                            authViewModel.resetState()
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.registerFragment, true)
                                .build()
                            findNavController().navigate(R.id.action_registerFragment_to_dashboardFragment, null, navOptions)
                        }
                        is AuthState.Error -> {
                            btnCreateAccount.isEnabled = true
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            authViewModel.resetState()
                        }
                        is AuthState.Idle -> {
                            btnCreateAccount.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
