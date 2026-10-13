package com.ddaypunk.aperture

import com.ddaypunk.aperture.screen.MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddaypunk.aperture.screen.MainScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
//        val database = createDatabase(DriverFactory(context = applicationContext))
        val viewmodel = MainScreenViewModel()
        setContent {
            val navController = rememberNavController()

            // TODO: very simple at this point just to have jetpack navigation in
            NavHost(
                navController = navController,
                startDestination = "main",
                modifier = Modifier.safeDrawingPadding()
            ) {
                composable("main") {
                    MainScreen(viewmodel)
                }
                // Something like the following for tapping a row to see more data
                // composable("details/{nomineeId}")
            }
        }
    }
}
