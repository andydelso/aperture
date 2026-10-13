package com.ddaypunk.aperture.presentation

/**
 * Platform-agnostic state for the main screen. Each platform maps these models to its own UI
 * components (Compose state wrappers on Android, SwiftUI views on iOS).
 */
sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Ready(val seasons: List<SeasonUiModel>) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
}

/**
 * One awards season, e.g. 2024
 */
data class SeasonUiModel(
    val year: String,
    val categories: List<CategoryUiModel>
)

/**
 * One award category within a season, with nominees sorted winners first, then by title
 */
data class CategoryUiModel(
    val title: String,
    val nominees: List<NomineeUiModel>
)

data class NomineeUiModel(
    val id: Long,
    val title: String,
    val isWatched: Boolean,
    val isWinner: Boolean,
    val image: String? = null
)
