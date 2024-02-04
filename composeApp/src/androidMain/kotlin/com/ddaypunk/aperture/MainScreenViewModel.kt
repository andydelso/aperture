package com.ddaypunk.aperture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.data.Award
import com.ddaypunk.aperture.data.Nominee
import com.ddaypunk.aperture.data.Season
import com.ddaypunk.aperture.db.ApertureDatabase
import com.ddaypunk.aperture.db.SelectAllAwardNominees
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
            val data = apertureDatabase.awardNomineeQueries.selectAllAwardNominees().executeAsList()
            val state = mapDataToState(data)
            // emit the state to the UI
            _uiState.update {
                MainScreenState.Ready(uiState = state)
            }
        }
    }

    private fun mapDataToState(data: List<SelectAllAwardNominees>) =
        MainScreenUiState(
            seasons = data.toListOfSeasons()
        )
}

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Ready(val uiState: MainScreenUiState) : MainScreenState()
}

data class MainScreenUiState(
    val seasons: List<Season>? = emptyList()
)

fun List<SelectAllAwardNominees>.toListOfSeasons(): List<Season> {
    return this
        .groupBy { it.awardYear } // map of year: Long to awardNominees: List<SelectAllAwardNominees>
        .mapNotNull { year ->
            Season(
                year = year.key.toInt(),
                awards = year.value.groupBy { it.categoryName }
                    .mapNotNull { category ->
                        Award(
                            category = category.key,
                            nominations = category.value.map { nominee ->
                                Nominee(
                                    name = nominee.name,
                                    secondary = nominee.secondaryInfo.split(","),
                                    won = nominee.won,
                                    note = nominee.notes
                                )
                            }
                        )
                    }
            )
        }
}