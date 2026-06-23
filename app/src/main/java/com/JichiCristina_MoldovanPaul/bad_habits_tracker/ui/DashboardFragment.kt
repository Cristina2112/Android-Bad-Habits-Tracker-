package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.RetrofitClient
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter.HabitAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var adapter: HabitAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDailyTip = view.findViewById<TextView>(R.id.tvDailyTip)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getRandomAdvice()
                tvDailyTip.text = "Sfatul zilei: ${response.slip.advice}"
            } catch (e: Exception) {
                tvDailyTip.text = "Rămâi puternic! Indiferent de ce se întâmplă, fii ferm pe poziție."
            }
        }

        val fabAddHabit = view.findViewById<FloatingActionButton>(R.id.fabAddHabit)
        fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addHabitFragment)
        }

        val btnLogout = view.findViewById<android.widget.Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val prefs = com.jichicristina_moldovanpaul.bad_habits_tracker.utils.SharedPrefsHelper(requireContext())
            prefs.isLoggedIn = false
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHabits)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = HabitAdapter { habit ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                try {
                    val rewardResponse = RetrofitClient.api.getRandomAdvice()
                    android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Încurajare pentru tine")
                        .setMessage(rewardResponse.slip.advice)
                        .setPositiveButton("OK", null)
                        .show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Conexiune la internet necesară pentru a prelua mesajul.", Toast.LENGTH_LONG).show()
                }
            }
        }
        recyclerView.adapter = adapter

        val database = AppDatabase.getDatabase(requireContext())
        val dao = database.habitDao()

        viewLifecycleOwner.lifecycleScope.launch {
            dao.getAllHabits().collect { habitsList ->
                adapter.submitList(habitsList)
            }
        }
    }
}
