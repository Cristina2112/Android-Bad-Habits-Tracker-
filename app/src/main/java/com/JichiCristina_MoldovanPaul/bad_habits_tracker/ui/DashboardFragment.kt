package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.os.Bundle
import android.util.Log
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
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AdviceEntity
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.RetrofitClient
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.models.GroqMessage
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.models.GroqRequest
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter.HabitAdapter
import com.jichicristina_moldovanpaul.bad_habits_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var adapter: HabitAdapter
    private var isAdviceFetched = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        val dao = database.habitDao()
        val adviceDao = database.adviceDao()
        val prefs = SharedPrefsHelper(requireContext())
        val userId = prefs.loggedInUserId

        val tvDailyTip = view.findViewById<TextView>(R.id.tvDailyTip)

        viewLifecycleOwner.lifecycleScope.launch {
            adviceDao.getLatestAdvice(userId).collect { advice ->
                if (advice != null) {
                    tvDailyTip.text = "Sfatul zilei: ${advice.adviceText}"
                } else {
                    tvDailyTip.text = "Rămâi puternic! Indiferent de ce se întâmplă, fii ferm pe poziție."
                }
            }
        }

        if (!isAdviceFetched) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                try {
                    android.util.Log.d("GEMINI_DEBUG", ">>> TRIMIT CERERE API PENTRU SFATUL ZILEI <<<")
                    val prompt = "Dă-mi un sfat scurt de o propoziție, puternic motivațional, în limba română, pentru o persoană care se luptă cu viciile."
                    val request = GroqRequest("llama-3.1-8b-instant", listOf(GroqMessage("user", prompt)))
                    val response = RetrofitClient.api.generateAdvice(request)
                    val text = response.choices?.firstOrNull()?.message?.content
                    if (text != null) {
                        adviceDao.insertAdvice(AdviceEntity(userId = userId, adviceText = text))
                        isAdviceFetched = true
                    }
                } catch (e: Exception) {
                    Log.e("GEMINI_ERROR", "Eroare API: ${e.message}")
                }
            }
        }

        val fabAddHabit = view.findViewById<FloatingActionButton>(R.id.fabAddHabit)
        fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addHabitFragment)
        }

        val btnLogout = view.findViewById<android.widget.Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            prefs.isLoggedIn = false
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHabits)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = HabitAdapter { habit ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Gândesc...", Toast.LENGTH_SHORT).show()
                }
                
                try {
                    val currentTime = System.currentTimeMillis()
                    val diffInMillis = currentTime - habit.startDate
                    val days = diffInMillis / (1000 * 60 * 60 * 24)

                    val prompt = "M-am lăsat de ${habit.habitName} de $days zile. Fă o glumă foarte scurtă, subtilă și de bun simț în română despre asta, ca să mă încurajezi."
                    android.util.Log.d("GEMINI_DEBUG", ">>> TRIMIT CERERE API PENTRU GLUMA: ${habit.habitName} <<<")
                    val request = GroqRequest("llama-3.1-8b-instant", listOf(GroqMessage("user", prompt)))
                    val response = RetrofitClient.api.generateAdvice(request)
                    val text = response.choices?.firstOrNull()?.message?.content

                    withContext(Dispatchers.Main) {
                        android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Încurajare pentru tine")
                            .setMessage(text ?: "[Sfat hardcodat] Funcția API a mers, dar răspunsul a fost gol (null).")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("GEMINI_ERROR", "Eroare API: ${e.message}")
                    withContext(Dispatchers.Main) {
                        android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Încurajare pentru tine")
                            .setMessage("[Mesaj hardcodat din Catch] EROARE: ${e.message}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
        }
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            dao.getHabitsForUser(userId).collect { habitsList ->
                adapter.submitList(habitsList)
            }
        }
    }
}
