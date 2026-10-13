package com.ddaypunk.aperture

import com.ddaypunk.aperture.screen.MainScreen
import android.os.Bundle
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddaypunk.aperture.presentation.MainScreenViewModel
import com.ddaypunk.aperture.theme.ApertureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // status bar sits over the red top app bar, so always use light icons
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
//        val database = createDatabase(DriverFactory(context = applicationContext))
        val viewmodel = MainScreenViewModel()
        setContent {
            ApertureTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    // TODO: very simple at this point just to have jetpack navigation in
                    NavHost(
                        navController = navController,
                        startDestination = "main"
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
    }
}
