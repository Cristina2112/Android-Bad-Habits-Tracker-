package com.jichicristina_moldovanpaul.bad_habits_tracker.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AppDatabase
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.HabitEntity
import com.jichicristina_moldovanpaul.bad_habits_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {

    private var selectedDateMillis: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnPickDate = view.findViewById<Button>(R.id.btnPickDate)
        val etHabitName = view.findViewById<EditText>(R.id.etHabitName)

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                selectedDateMillis = selectedCalendar.timeInMillis
                btnPickDate.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            }, year, month, day)

            datePickerDialog.show()
        }

        btnSave.setOnClickListener {
            val habitName = etHabitName.text.toString().trim()

            if (habitName.isEmpty()) {
                Toast.makeText(requireContext(), "Introduceți numele viciului!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDateMillis == null) {
                Toast.makeText(requireContext(), "Alegeți data de start!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = SharedPrefsHelper(requireContext())
            val currentUserId = prefs.loggedInUserId

            val noulViciu = HabitEntity(
                habitName = habitName,
                startDate = selectedDateMillis!!,
                userId = currentUserId
            )

            val dao = AppDatabase.getDatabase(requireContext()).habitDao()

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                dao.insertHabit(noulViciu)
                
                withContext(Dispatchers.Main) {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
