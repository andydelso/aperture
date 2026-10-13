package com.ddaypunk.aperture.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Handle for a Swift-side subscription; call [close] to stop receiving updates
 */
class UiStateSubscription internal constructor(private val job: Job) {
    fun close() {
        job.cancel()
    }
}

/**
 * Swift-friendly way to observe [MainScreenViewModel.uiState], since Swift can't collect a Kotlin Flow directly.
 * [onChange] is called on the main thread with the current state and every update after it.
 */
fun MainScreenViewModel.observeUiState(onChange: (MainScreenState) -> Unit): UiStateSubscription =
    UiStateSubscription(viewModelScope.launch { uiState.collect { onChange(it) } })
