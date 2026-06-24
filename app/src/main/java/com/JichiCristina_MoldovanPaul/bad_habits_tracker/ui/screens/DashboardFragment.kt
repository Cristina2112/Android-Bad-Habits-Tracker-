package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import com.jichicristina_moldovanpaul.bad_habits_tracker.network.RetrofitClient
import com.jichicristina_moldovanpaul.bad_habits_tracker.network.models.GroqMessage
import com.jichicristina_moldovanpaul.bad_habits_tracker.network.models.GroqRequest
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter.HabitAdapter
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.DashboardViewModel
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.DashboardViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val dashboardViewModel: DashboardViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val dataStore = AuthDataStore(requireContext())
        DashboardViewModelFactory(database.habitDao(), dataStore)
    }

    private lateinit var habitAdapter: HabitAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDailyTip = view.findViewById<TextView>(R.id.tvDailyTip)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val rvHabits = view.findViewById<RecyclerView>(R.id.rvHabits)
        val fabAddHabit = view.findViewById<FloatingActionButton>(R.id.fabAddHabit)

        habitAdapter = HabitAdapter { habit ->
            val currentTime = System.currentTimeMillis()
            val diffInMillis = currentTime - habit.startDate
            val days = diffInMillis / (1000 * 60 * 60 * 24)
            val zileStr = if (days == 1L) "o zi" else "$days zile"

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val request = GroqRequest(
                        model = "llama-3.3-70b-versatile",
                        messages = listOf(
                            GroqMessage(role = "system", content = "Ești un prieten glumeț și motivant. Răspunde exclusiv în limba română corectă gramatical. Nu folosi caractere speciale sau formatare markdown (fără steluțe). Răspunde scurt (maxim 2 propoziții)."),
                            GroqMessage(role = "user", content = "M-am lăsat de ${habit.habitName} de $zileStr. Fă o glumă scurtă despre asta ca să mă motivezi să rezist!")
                        )
                    )
                    val response = RetrofitClient.api.generateAdvice(request)
                    withContext(Dispatchers.Main) {
                        val advice = response.choices?.firstOrNull()?.message?.content ?: "Fii puternic în continuare!"
                        AlertDialog.Builder(requireContext())
                            .setTitle("Sfat pentru ${habit.habitName}")
                            .setMessage(advice)
                            .setPositiveButton("Am înțeles", null)
                            .show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Eroare rețea", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        rvHabits.layoutManager = LinearLayoutManager(requireContext())
        rvHabits.adapter = habitAdapter

        // Fetch advice at launch
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = GroqRequest(
                    model = "llama-3.3-70b-versatile",
                    messages = listOf(
                        GroqMessage(role = "system", content = "Dă-mi un citat motivațional scurt (o propoziție). Scrie exclusiv în limba română corectă, fără formatări markdown (fără steluțe) și fără alte introduceri.")
                    )
                )
                val response = RetrofitClient.api.generateAdvice(request)
                withContext(Dispatchers.Main) {
                    val advice = response.choices?.firstOrNull()?.message?.content
                    if (advice != null) {
                        tvDailyTip.text = "Sfatul zilei: $advice"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvDailyTip.text = "Sfatul zilei: Fii mai bun decât ieri!"
                }
            }
        }

        // Observe Habits from View Model
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.habitsFlow.collect { habits ->
                    habitAdapter.submitList(habits)
                }
            }
        }

        btnLogout.setOnClickListener {
            dashboardViewModel.logout()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.dashboardFragment, true)
                .build()
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment, null, navOptions)
        }

        val btnTherapy = view.findViewById<Button>(R.id.btnTherapy)
        btnTherapy.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_therapyFragment)
        }

        fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addHabitFragment)
        }
    }
}
