package com.ddaypunk.aperture.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class DetailsScreenViewModel : ViewModel(), KoinComponent {
    private val _uiState =
        MutableStateFlow<DetailsScreenState>(DetailsScreenState.Loading)
    val uiState: StateFlow<DetailsScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

//            _uiState.update { DetailsScreenViewModelUiState.Ready() }
        }
    }

    // private fun onUserInput(userInput: DetailsScreenViewModelUserInput) {}
}

sealed class DetailsScreenState {
    data object Loading : DetailsScreenState()
    data class Ready(val nomineeDetails: NomineeDetails) : DetailsScreenState()
    data class Error(val message: String) : DetailsScreenState()
}

data class NomineeDetails(
    val id: Int,
    val name: String
)

// sealed class DetailsScreenViewModelUserInput {}
