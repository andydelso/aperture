package com.ddaypunk.aperture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ddaypunk.aperture.screen.DetailsScreen
import com.ddaypunk.aperture.screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // TODO: very simple at this point just to have jetpack navigation in
            NavHost(navController = navController, startDestination = "main") {
                composable(
                    route = "main"
                ) {
                    MainScreen(
                        onNavigateTo = { nomineeId ->
                            navController.navigate("details/$nomineeId")
                        }
                    )
                }
                composable(
                    route = "details/{nomineeId}",
                    arguments = listOf(navArgument("nomineeId") { type = NavType.LongType })
                ) {
                    DetailsScreen(
                        nomineeId = it.arguments?.getLong("nomineeId"),
                    )
                }
            }
        }
    }
}
