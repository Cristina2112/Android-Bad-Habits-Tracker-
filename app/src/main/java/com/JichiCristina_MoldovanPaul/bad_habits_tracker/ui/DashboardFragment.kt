package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter.HabitAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var adapter: HabitAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restauram butonul de Plus care fusese sters
        val fabAddHabit = view.findViewById<FloatingActionButton>(R.id.fabAddHabit)
        fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addHabitFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHabits)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = HabitAdapter()
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
