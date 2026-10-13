package com.ddaypunk.aperture

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ddaypunk.aperture.navigation.ApertureNavDisplay
import com.ddaypunk.aperture.navigation.Navigator
import com.ddaypunk.aperture.presentation.MainScreenViewModel
import com.ddaypunk.aperture.theme.ApertureTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        // status bar sits over the red top app bar, so always use light icons
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        val viewmodel = MainScreenViewModel()
        setContent {
            ApertureTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ApertureNavDisplay(
                        navigator = navigator,
                        mainScreenViewModel = viewmodel
                    )
                }
            }
        }
    }
}
