package com.jichicristina_moldovanpaul.bad_habits_tracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.local.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        val dataStore = AuthDataStore(this)
        val loggedInUserId = runBlocking { dataStore.loggedInUserIdFlow.first() }

        if (loggedInUserId != -1) {
            navGraph.setStartDestination(R.id.dashboardFragment)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }
        navController.graph = navGraph
    }
}
