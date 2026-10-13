package com.ddaypunk.aperture.presentation

import ApertureDatabaseRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class NomineeDetailState {
    data object Loading : NomineeDetailState()
    data class Ready(val title: String) : NomineeDetailState()
    data object NotFound : NomineeDetailState()
}

/**
 * Placeholder details for one nominee, looked up by id. Shows the title for now.
 */
class NomineeDetailViewModel(private val nomineeId: Long) : ViewModel(), KoinComponent {
    private val repository: ApertureDatabaseRepository by inject()
    private val navigator: Navigator by inject()

    private val _uiState = MutableStateFlow<NomineeDetailState>(NomineeDetailState.Loading)
    val uiState: StateFlow<NomineeDetailState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getNomineeFlow(nomineeId)
                .catch { _uiState.value = NomineeDetailState.NotFound }
                .collect { nominee ->
                    _uiState.value = nominee
                        ?.let { NomineeDetailState.Ready(title = it.name) }
                        ?: NomineeDetailState.NotFound
                }
        }
    }

    fun onBack() {
        navigator.pop()
    }
}
