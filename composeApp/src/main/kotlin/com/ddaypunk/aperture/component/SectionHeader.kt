package com.ddaypunk.aperture.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
            // sticky header: opaque so scrolled cards don't show through
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            text = state.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Immutable
data class SectionHeaderState(
    val title: String
)