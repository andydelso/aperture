package com.ddaypunk.aperture.screen

import ApertureDatabaseRepository
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaypunk.aperture.R
import com.ddaypunk.aperture.component.CheckableRowState
import com.ddaypunk.aperture.component.ExpandableCardState
import com.ddaypunk.aperture.db.SelectAllAwardNominees
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreenViewModel(

) : ViewModel(), KoinComponent {
    private val repository: ApertureDatabaseRepository by inject()

    private val _uiState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val repositoryFlow = repository.getAllAwardNomineesFlow()

            repositoryFlow.onStart { _uiState.emit(MainScreenState.Loading) }
                .catch { _uiState.emit(MainScreenState.Error("Error retrieving data from the database")) }
                .collect { data ->
                    _uiState.emit(
                        MainScreenState.Ready(
                            seasons = data.toListOfSeasonEntryStates()
                        )
                    )
                }
        }
    }

    fun onCheckboxToggle(rowId: Long, isChecked: Boolean) {
        repository.updateNomineeWatched(rowId, isChecked)
    }

    private fun List<SelectAllAwardNominees>.toListOfSeasonEntryStates(): List<SeasonEntryState> {
        return this
            .groupBy { it.awardYear }
            .mapNotNull { year ->
                mapSeasonEntryState(year)
            }
    }

    /**
     * Create the state for each awards season
     * @return [SeasonEntryState]
     */
    private fun mapSeasonEntryState(year: Map.Entry<Long, List<SelectAllAwardNominees>>) =
        SeasonEntryState(
            title = year.key.toString(),
            categoryStates = year.value.groupBy { it.categoryName }
                .mapNotNull { category ->
                    mapExpandableCardState(category)
                }
        )

    /**
     * Create the state for each expandable card using DB data models
     * @return [ExpandableCardState] with nominees sorted by winner at top, then by title
     */
    private fun mapExpandableCardState(category: Map.Entry<String, List<SelectAllAwardNominees>>): ExpandableCardState {
        val sorted = category.value.sortedWith(
            compareBy({ !it.won }, { it.name })
        )

        return ExpandableCardState(
            title = category.key,
            nomineeStates = sorted.map { nominee ->
                mapCheckableRowState(nominee)
            }
        )
    }

    private fun mapCheckableRowState(nominee: SelectAllAwardNominees) =
        CheckableRowState(
            rowId = nominee.id,
            title = nominee.name,
            isChecked = nominee.watched,
            onCheckedChange = {
                onCheckboxToggle(
                    rowId = nominee.id,
                    isChecked = !nominee.watched
                )
            },
            isWinner = nominee.won
        )

    private fun getWinnerIcon(isWinner: Boolean): Int? {
        return if (isWinner) R.drawable.ic_trophy_24 else null
    }
}

@Stable
sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Ready(val seasons: List<SeasonEntryState>) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
}

@Stable
data class SeasonEntryState(
    val title: String,
    val categoryStates: List<ExpandableCardState>
)