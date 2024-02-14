package com.ddaypunk.aperture.screen

import ApertureDatabaseRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.R
import com.ddaypunk.aperture.component.CheckableRowState
import com.ddaypunk.aperture.component.ExpandableCardState
import com.ddaypunk.aperture.db.SelectAllAwardNominees
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreenViewModel(
) : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()
    private val repository: ApertureDatabaseRepository by inject()

    init {
        viewModelScope.launch {
            val state = mapDataToState(repository.getAllAwardNominees())
            // emit the state to the UI
            _uiState.update {
                MainScreenState.Ready(uiState = state)
            }
        }
    }

    private fun mapDataToState(data: List<SelectAllAwardNominees>) =
        MainScreenUiState(
            seasons = data.toListOfSeasonEntryStates()
        )

    fun onCheckboxToggle(rowId: Long, isChecked: Boolean) {
        repository.updateNomineeWatched(rowId, isChecked)

        val state = mapDataToState(repository.getAllAwardNominees())
        // emit the state to the UI
        _uiState.update {
            MainScreenState.Ready(uiState = state)
        }
    }

    private fun List<SelectAllAwardNominees>.toListOfSeasonEntryStates(): List<SeasonEntryState> {
        return this
            .groupBy { it.awardYear } // map of year: Long to awardNominees: List<SelectAllAwardNominees>
            .mapNotNull { year ->
                SeasonEntryState(
                    title = year.key.toString(),
                    categoryStates = year.value.groupBy { it.categoryName }
                        .mapNotNull { category ->
                            ExpandableCardState(
                                title = category.key,
                                nomineeStates = category.value.map { nominee ->
                                    CheckableRowState(
                                        rowId = nominee.id,
                                        title = nominee.name,
                                        isChecked = nominee.watched,
                                        onCheckedChange = { onCheckboxToggle(nominee.id, !nominee.watched) },
                                        endIcon = if (nominee.won) R.drawable.ic_trophy_24 else null
//                                    secondary = nominee.secondaryInfo.split(","),
//                                    note = nominee.notes
                                    )
                                }
                            )
                        }
                )
            }
    }
}

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Ready(val uiState: MainScreenUiState) : MainScreenState()
}

data class MainScreenUiState(
    val seasons: List<SeasonEntryState>? = emptyList()
)

data class SeasonEntryState(
    val title: String,
    val categoryStates: List<ExpandableCardState>
)