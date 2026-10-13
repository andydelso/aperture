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
import com.ddaypunk.aperture.component.ExpandableCard
import com.ddaypunk.aperture.component.ExpandableCardAccessibilityState
import com.ddaypunk.aperture.component.SectionHeader
import com.ddaypunk.aperture.presentation.CategoryUiModel
import com.ddaypunk.aperture.presentation.MainScreenState
import com.ddaypunk.aperture.presentation.MainScreenViewModel
import com.ddaypunk.aperture.presentation.NomineeUiModel
import com.ddaypunk.aperture.presentation.SeasonUiModel
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
                onWatchedToggle = viewModel::onWatchedToggle,
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
    onWatchedToggle: (nomineeId: Long, isWatched: Boolean) -> Unit,
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
        state.seasons.forEach { season ->
            stickyHeader {
                SectionHeader(state = season.toSectionHeaderState())
            }
            items(season.categories) { category ->
                ExpandableCard(
                    state = category.toExpandableCardState(onWatchedToggle),
                    accessibilityState = ExpandableCardAccessibilityState(
                        seasonYear = season.year
                    )
                )
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
                        SeasonUiModel(
                            year = "2024",
                            categories = listOf(
                                CategoryUiModel(
                                    title = "Best Previews Ever",
                                    nominees = listOf(
                                        NomineeUiModel(
                                            id = 1,
                                            title = "Star Wars: The Force Awakens",
                                            isWatched = true,
                                            isWinner = true
                                        ),
                                        NomineeUiModel(
                                            id = 2,
                                            title = "Harry Potter and the Order of the Phoenix",
                                            isWatched = true,
                                            isWinner = false
                                        ),
                                        NomineeUiModel(
                                            id = 3,
                                            title = "Space Balls II: The Search for More Money",
                                            isWatched = false,
                                            isWinner = false
                                        ),
                                    )
                                )
                            )
                        )
                    )
                ),
                onWatchedToggle = { _, _ -> }
            )
        }
    }
}
