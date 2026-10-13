package com.ddaypunk.aperture.presentation

import ApertureDatabaseRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Shared view model for the main screen, used by both the Android and iOS apps
 */
class MainScreenViewModel : ViewModel(), KoinComponent {
    private val repository: ApertureDatabaseRepository by inject()
    private val navigator: Navigator by inject()

    private val _uiState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getBestPictureNominationsFlow()
                .onStart { _uiState.value = MainScreenState.Loading }
                .catch { _uiState.value = MainScreenState.Error("Error retrieving data from the database") }
                .collect { rows ->
                    _uiState.value = MainScreenState.Ready(seasons = rows.toSeasonUiModels())
                }
        }
    }

    fun onWatchedToggle(nomineeId: Long, isWatched: Boolean) {
        repository.updateFilmWatched(nomineeId, isWatched)
    }

    fun onNomineeSelected(nomineeId: Long) {
        navigator.openNomineeDetail(nomineeId)
    }
}
