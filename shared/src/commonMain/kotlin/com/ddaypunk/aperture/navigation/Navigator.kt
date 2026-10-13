package com.ddaypunk.aperture.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Destinations in the app. Routes carry only ids; screens look up what they need.
 */
sealed class Route {
    data object Main : Route()
    data class NomineeDetail(val nomineeId: Long) : Route()
}

/**
 * Owns the app's back stack so both platforms render the same navigation state.
 * Android's NavDisplay and iOS's NavigationSplitView read [backStack]; user and system
 * back actions on either platform come back through [pop] / [popToRoot].
 */
class Navigator(private val root: Route = Route.Main) {
    private val _backStack = MutableStateFlow(listOf(root))
    val backStack: StateFlow<List<Route>> = _backStack.asStateFlow()

    /**
     * Shows a nominee's details. List-detail behavior: replaces a details screen that is already
     * showing instead of stacking another one on top of it.
     */
    fun openNomineeDetail(nomineeId: Long) {
        _backStack.update { stack ->
            stack.filterNot { it is Route.NomineeDetail } + Route.NomineeDetail(nomineeId)
        }
    }

    /**
     * Removes the top route. The root route is never removed.
     * @return true if a route was removed
     */
    fun pop(): Boolean {
        if (_backStack.value.size <= 1) return false
        _backStack.update { it.dropLast(1) }
        return true
    }

    fun popToRoot() {
        _backStack.value = listOf(root)
    }
}
