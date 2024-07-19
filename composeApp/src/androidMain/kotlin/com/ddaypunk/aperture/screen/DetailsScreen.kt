package com.ddaypunk.aperture.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailsScreen(
    nomineeId: Long?,
    viewModel: DetailsScreenViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()

    when (state.value) {
        is DetailsScreenState.Ready -> {
            DetailsScreenReady(
                state.value as DetailsScreenState.Ready,
                nomineeId = nomineeId
            )
        }

        is DetailsScreenState.Loading -> MainScreenLoading()
        else -> {
            // TODO: handle error state
        }
    }
}

@Composable
fun DetailsScreenReady(
    state: DetailsScreenState,
    nomineeId: Long?
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        nomineeId?.let {
            Text(text = it.toString())
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {

    DetailsScreenReady(
        state = DetailsScreenState.Ready(
            nomineeDetails = NomineeDetails(
                id = 0,
                name = "Star Wars: The Force Awakens",
//                type = "Movie",
//                watched = false,
//                secondaryInfo = "Stuff",
//                imdbID = "1234ID"
            )
        ),
        nomineeId = 12345L
    )
}