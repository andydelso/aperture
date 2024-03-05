package com.ddaypunk.aperture.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

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
        ) {

            Checkbox(
                modifier = Modifier.padding(8.dp),
                checked = isChecked,
                onCheckedChange = { onCheckedChange.invoke() }
            )
            Text(
                modifier = Modifier.weight(1f),
                text = title, fontSize = 18.sp
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
            endIcon?.let { nonNullEndIcon ->
                Image(
                    modifier = Modifier.padding(end = 16.dp),
                    painter = painterResource(nonNullEndIcon),
                    contentDescription = "category winner",
                    colorFilter = ColorFilter.tint(Color.Yellow)
                )
            }
        }
    }
}

@Preview
@Composable
fun CheckableRowPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            CheckableRow(
                state = CheckableRowState(
                    rowId = 1,
                    title = "Star Wars: The Force Awakens",
                    isChecked = false,
                    onCheckedChange = {},
                    endIcon = null
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
    @DrawableRes val endIcon: Int? = null,
)