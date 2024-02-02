package com.ddaypunk.aperture

import DriverFactory
import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import createDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = createDatabase(DriverFactory(context = applicationContext))

        setContent {
            val navController = rememberNavController()

            // TODO: very simple at this point just to have jetpack navigation in
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    MainScreen(MainScreenViewModel(database))
                }
                // Something like the following for tapping a row to see more data
                // composable("details/{nomineeId}")
            }
        }
    }
}
