package com.ddaypunk.aperture.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ddaypunk.aperture.R
import com.ddaypunk.aperture.presentation.NomineeDetailState
import com.ddaypunk.aperture.presentation.NomineeDetailViewModel
import com.ddaypunk.aperture.theme.ApertureTheme
import com.ddaypunk.aperture.theme.OscarsLightGold
import com.ddaypunk.aperture.theme.OscarsRed

/**
 * Placeholder details for a nominee; shows its title for now
 */
@Composable
fun NomineeDetailScreen(viewModel: NomineeDetailViewModel) {
    val state by viewModel.uiState.collectAsState()
    NomineeDetailContent(state = state, onBack = viewModel::onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NomineeDetailContent(
    state: NomineeDetailState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OscarsRed,
                    navigationIconContentColor = OscarsLightGold
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is NomineeDetailState.Ready -> Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                is NomineeDetailState.NotFound -> Text(
                    text = "Nominee not found",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                is NomineeDetailState.Loading -> CircularProgressIndicator()
            }
        }
    }
}

/**
 * Shown in the details pane on wide screens before a nominee is selected
 */
@Composable
fun NomineeDetailPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Select a nominee to see details",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun NomineeDetailContentPreview() {
    ApertureTheme {
        Surface {
            NomineeDetailContent(
                state = NomineeDetailState.Ready(title = "Oppenheimer"),
                onBack = {}
            )
        }
    }
}
