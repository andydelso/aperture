package com.ddaypunk.aperture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.data.Award
import com.ddaypunk.aperture.data.Nominee
import com.ddaypunk.aperture.data.Season
import com.ddaypunk.aperture.db.ApertureDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val apertureDatabase: ApertureDatabase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Todo: just getting the one and only category for now, to test the waters
            val element = apertureDatabase.categoriesQueries.selectAll().executeAsList()[0].name
            // emit the state to the UI
            _uiState.update {
                // TODO: remove mocks
                MainScreenState.Ready(
                    uiState = MainScreenUiState(
                        seasons = listOf(
                            Season(
                                year = 2022,
                                awards = listOf(
                                    Award(
                                        id = null,
                                        category = element,
                                        nominations = listOf(
                                            Nominee(
                                                id = null,
                                                name = "CODA (Mock)",
                                                won = true
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "Belfast (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "Don't Look Up (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "Drive My Car (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "Dune (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "King Richard (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "Licorice Pizza (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "Nightmare Alley (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "The Power of the Dog (Mock)",
                                                won = false
                                            ),
                                            Nominee(
                                                id = null,
                                                name = "West Side Story (Mock)",
                                                won = false
                                            ),
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            }
        }
    }
}

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Ready(val uiState: MainScreenUiState) : MainScreenState()
}

data class MainScreenUiState(
    val seasons: List<Season>? = emptyList()
)