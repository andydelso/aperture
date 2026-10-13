package com.ddaypunk.aperture.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ddaypunk.aperture.R
import com.ddaypunk.aperture.component.CheckableRowState
import com.ddaypunk.aperture.component.ExpandableCard
import com.ddaypunk.aperture.component.ExpandableCardAccessibilityState
import com.ddaypunk.aperture.component.ExpandableCardState
import com.ddaypunk.aperture.component.SectionHeader
import com.ddaypunk.aperture.component.SectionHeaderState
import com.ddaypunk.aperture.theme.ApertureTheme
import com.ddaypunk.aperture.theme.OscarsLightGold
import com.ddaypunk.aperture.theme.OscarsRed

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { MainTopAppBar() }
    ) { innerPadding ->
        when (state.value) {
            is MainScreenState.Ready -> MainScreenReady(
                state = state.value as MainScreenState.Ready,
                contentPadding = innerPadding
            )
            is MainScreenState.Loading -> MainScreenLoading(
                modifier = Modifier.padding(innerPadding)
            )
            else -> {
                // TODO: handle error state
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = OscarsRed,
            titleContentColor = OscarsLightGold
        )
    )
}

@Composable
fun MainScreenLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Loading...")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenReady(
    state: MainScreenState.Ready,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
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
    ApertureTheme {
        Surface {
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
}
