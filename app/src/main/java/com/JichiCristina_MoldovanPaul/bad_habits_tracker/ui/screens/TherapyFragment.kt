package com.jichicristina_moldovanpaul.bad_habits_tracker.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jichicristina_moldovanpaul.bad_habits_tracker.R
import com.jichicristina_moldovanpaul.bad_habits_tracker.network.DogRetrofitClient
import com.jichicristina_moldovanpaul.bad_habits_tracker.ui.adapter.DogAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TherapyFragment : Fragment(R.layout.fragment_therapy) {

    private lateinit var dogAdapter: DogAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvDogs = view.findViewById<RecyclerView>(R.id.rvDogs)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarTherapy)

        dogAdapter = DogAdapter()
        rvDogs.layoutManager = LinearLayoutManager(requireContext())
        rvDogs.adapter = dogAdapter

        fetchDogs(progressBar)
    }

    private fun fetchDogs(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dogs = DogRetrofitClient.api.getDogs()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    dogAdapter.submitList(dogs)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Eroare la încărcarea animaluțelor :(", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
