package com.ddaypunk.aperture.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a single date header representing one season of an awards show
 * @param state [SectionHeaderState] object describing the year to display
 */
@Composable
fun SectionHeader(
    state: SectionHeaderState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = state.title,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Immutable
data class SectionHeaderState(
    val title: String
)