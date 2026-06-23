package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.UserEntity
import com.jichicristina_moldovanpaul.bad_habits_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                val dao = AppDatabase.getDatabase(requireContext()).userDao()
                val newUser = UserEntity(username = username, password = password, motivationMsg = motivation)
                
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val newId = dao.insertUser(newUser).toInt()
                    
                    withContext(Dispatchers.Main) {
                        val prefs = SharedPrefsHelper(requireContext())
                        prefs.loggedInUserId = newId

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.loginFragment, true)
                            .build()
                        findNavController().navigate(R.id.dashboardFragment, null, navOptions)
                    }
                }
            }
        }

        tvBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}
