package com.ddaypunk.aperture.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ddaypunk.aperture.theme.ApertureTheme

/**
 * A component to display a row of data with a checkbox and a possible end of row icon
 * @param state [CheckableRowState] to specify the title and if the icon displays at the end
 * - Note: The id is used when the row is checked to tell the view model to update the related
 * db row
 */
@Composable
fun CheckableRow(
    state: CheckableRowState
) {
    with(state) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 16.dp)
        ) {

            Checkbox(
                modifier = Modifier.padding(8.dp),
                checked = isChecked,
                onCheckedChange = { onCheckedChange.invoke() }
            )
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            image?.let { nonNullImage ->
                AsyncImage(
                    model = nonNullImage,
                    contentDescription = "$title move poster",
                    contentScale = ContentScale.Crop,
                    placeholder = BrushPainter(
                        Brush.linearGradient(
                            listOf(
                                Color(color = 0xFFFFFFFF),
                                Color(color = 0xFFDDDDDD),
                            )
                        )
                    )
                )
            }
            if (isWinner) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "category winner")
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckableRowPreview() {
    ApertureTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CheckableRow(
                state = CheckableRowState(
                    rowId = 1,
                    title = "Star Wars: The Force Awakens",
                    isChecked = false,
                    onCheckedChange = {},
                    isWinner = false
                )
            )
        }
    }
}

@Immutable
data class CheckableRowState(
    val rowId: Long,
    val title: String,
    val isChecked: Boolean,
    val onCheckedChange: () -> Unit,
    val image: String? = null,
    val isWinner: Boolean = false
)