package com.ddaypunk.aperture.component

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ddaypunk.aperture.R

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
    // This will need to be in the ViewModel later
    val checked = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.toggleable(
            value = checked.value,
            enabled = true,
            role = Role.Checkbox
        ) { isChecked ->
            checked.value = isChecked
        }
    ) {

        Checkbox(
            checked = checked.value,
            enabled = true,
            onCheckedChange = { isChecked ->
                checked.value = isChecked
            }
        )
        Text(
            modifier = Modifier.weight(1f),
            text = state.title, fontSize = 18.sp
        )
        state.endIcon?.let { nonNullEndIcon ->
            Image(
                modifier = Modifier.padding(end = 16.dp),
                painter = painterResource(nonNullEndIcon),
                contentDescription = "category winner",
                colorFilter = ColorFilter.tint(Color.Yellow)
            )
        }
    }
}

data class CheckableRowState(
    val rowId: Long,
    val title: String,
    @DrawableRes val endIcon: Int? = null,
)