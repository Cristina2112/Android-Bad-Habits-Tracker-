package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.AddHabitViewModel
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.viewModels.AddHabitViewModelFactory
import java.util.Calendar

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {

    private val addHabitViewModel: AddHabitViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val dataStore = AuthDataStore(requireContext())
        AddHabitViewModelFactory(database.habitDao(), dataStore)
    }

    private var selectedDateMillis: Long = System.currentTimeMillis()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etHabitName = view.findViewById<EditText>(R.id.etHabitName)
        val btnPickDate = view.findViewById<Button>(R.id.btnPickDate)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(year, month, dayOfMonth)
                    selectedDateMillis = selectedCal.timeInMillis
                    btnPickDate.text = "$dayOfMonth/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSave.setOnClickListener {
            val habitName = etHabitName.text.toString()

            if (habitName.isBlank()) {
                Toast.makeText(requireContext(), "Introdu un nume pentru viciu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addHabitViewModel.saveHabit(habitName, selectedDateMillis) {
                Toast.makeText(requireContext(), "Viciu salvat!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
}
