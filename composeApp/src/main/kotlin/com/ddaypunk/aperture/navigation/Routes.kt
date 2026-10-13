package com.ddaypunk.aperture.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Navigation 3 keys. Serializable so rememberNavBackStack can save the back stack
 * across configuration changes and process death.
 */
@Serializable
data object MainRoute : NavKey
