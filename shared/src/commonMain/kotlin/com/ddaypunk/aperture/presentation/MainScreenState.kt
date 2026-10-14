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
 * One awards season (ceremony)
 * @param year display label with the ceremony year and number, e.g. "2026 · 98th"
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

/**
 * @param id the film id, used for the watched toggle and detail navigation
 */
data class NomineeUiModel(
    val id: Long,
    val title: String,
    val isWatched: Boolean,
    val isWinner: Boolean,
    val image: String? = null
)
