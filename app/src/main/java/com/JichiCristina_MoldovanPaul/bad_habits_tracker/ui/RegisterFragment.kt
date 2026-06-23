package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.utils.SharedPrefsHelper

class RegisterFragment : Fragment(R.layout.fragment_register) {

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
                Toast.makeText(requireContext(), "Te rog completează toate datele!", Toast.LENGTH_SHORT).show()
            } else {
                val prefs = SharedPrefsHelper(requireContext())
                prefs.username = username
                prefs.password = password
                prefs.motivationMsg = motivation
                prefs.isLoggedIn = true

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
                findNavController().navigate(R.id.dashboardFragment, null, navOptions)
            }
        }

        tvBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}
