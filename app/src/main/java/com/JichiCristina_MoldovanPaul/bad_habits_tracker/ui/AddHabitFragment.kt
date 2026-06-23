package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jichicristina_moldovanpaul.bad_habits_tracker.R

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_addHabitFragment_to_dashboardFragment)
        }
    }
}
