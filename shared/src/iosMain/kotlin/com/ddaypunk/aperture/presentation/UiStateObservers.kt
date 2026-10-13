package com.ddaypunk.aperture.presentation

import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.navigation.Navigator
import com.ddaypunk.aperture.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Swift can't collect a Kotlin Flow directly, so these expose typed, callback-based observers.
// Each callback runs on the main thread with the current value and every update after it.

/**
 * Handle for a Swift-side subscription; call [close] to stop receiving updates
 */
class UiStateSubscription internal constructor(private val job: Job) {
    fun close() {
        job.cancel()
    }
}

private fun <T> StateFlow<T>.observe(scope: CoroutineScope, onChange: (T) -> Unit) =
    UiStateSubscription(scope.launch { collect { onChange(it) } })

fun MainScreenViewModel.observeUiState(onChange: (MainScreenState) -> Unit): UiStateSubscription =
    uiState.observe(viewModelScope, onChange)

fun NomineeDetailViewModel.observeUiState(onChange: (NomineeDetailState) -> Unit): UiStateSubscription =
    uiState.observe(viewModelScope, onChange)

fun Navigator.observeBackStack(onChange: (List<Route>) -> Unit): UiStateSubscription =
    backStack.observe(MainScope(), onChange)
