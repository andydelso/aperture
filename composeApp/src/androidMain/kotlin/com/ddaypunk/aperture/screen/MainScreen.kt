package com.ddaypunk.aperture.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ddaypunk.aperture.component.CheckableRowState
import com.ddaypunk.aperture.component.ExpandableCard
import com.ddaypunk.aperture.component.ExpandableCardAccessibilityState
import com.ddaypunk.aperture.component.ExpandableCardState
import com.ddaypunk.aperture.component.SectionHeader
import com.ddaypunk.aperture.component.SectionHeaderState

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel
) {
    val state = viewModel.uiState.collectAsState()

    when (state.value) {
        is MainScreenState.Ready -> MainScreenReady(state.value as MainScreenState.Ready)
        is MainScreenState.Loading -> MainScreenLoading()
        else -> {
            // TODO: handle error state
        }
    }
}

@Composable
fun MainScreenLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Loading...")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenReady(state: MainScreenState.Ready) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        with(state) {
            seasons.let { nonNullSeasons ->
                nonNullSeasons.forEach { season ->
                    stickyHeader {
                        SectionHeader(
                            state = SectionHeaderState(
                                title = season.title
                            )
                        )
                    }
                    items(season.categoryStates) { category ->
                        ExpandableCard(
                            state = ExpandableCardState(
                                title = category.title,
                                nomineeStates = category.nomineeStates
                            ),
                            accessibilityState = ExpandableCardAccessibilityState(
                                seasonYear = season.title
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenReadyPreview() {
    MaterialTheme {
        MainScreenReady(
            state = MainScreenState.Ready(
                seasons = listOf(
                    SeasonEntryState(
                        title = "2024",
                        categoryStates = listOf(
                            ExpandableCardState(
                                title = "Best Previews Ever",
                                nomineeStates = listOf(
                                    CheckableRowState(
                                        rowId = 1,
                                        title = "Star Wars: The Force Awakens",
                                        isChecked = true,
                                        onCheckedChange = {}
                                    ),
                                    CheckableRowState(
                                        rowId = 2,
                                        title = "Harry Potter and the Order of the Phoenix",
                                        isChecked = true,
                                        onCheckedChange = {}
                                    ),
                                    CheckableRowState(
                                        rowId = 3,
                                        title = "Space Balls II: The Search for More Money",
                                        isChecked = true,
                                        onCheckedChange = {}
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
