package com.ddaypunk.aperture.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Card
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ddaypunk.aperture.R

/**
 * A component to display an card that can hide or show rows of items on click
 * @param state [ExpandableCardState] contains the title information to display
 * and a list of item states to display when expanded
 * @param accessibilityState [ExpandableCardAccessibilityState] holds the year from the header
 * to provide better context to a11y users of which expandable card they are using if there are
 * a lot on the screen
 */
@Composable
fun ExpandableCard(
    state: ExpandableCardState,
    accessibilityState: ExpandableCardAccessibilityState,
) {
    // TODO: move this into the VM with actions - this can allow us to expand the first by default
    var showNominations by remember { mutableStateOf(false) }
    Card {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .toggleable(
                        value = showNominations,
                        enabled = true,
                        role = Role.DropdownList
                    ) { rowExpanded ->
                        showNominations = rowExpanded
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = state.title,
                    fontSize = 20.sp
                )
                IconToggleButton(
                    checked = showNominations,
                    onCheckedChange = { showNominations = !showNominations },
                    enabled = true
                ) {
                    if (showNominations) {
                        Image(
                            painter = painterResource(R.drawable.ic_expand_less_24),
                            contentDescription = "${accessibilityState.seasonYear} ${state.title} show less"
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_expand_more_24),
                            contentDescription = "${accessibilityState.seasonYear} ${state.title} show more"
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showNominations
            ) {
                Column {
                    state.nomineeStates.forEach { nominee ->
                        key(nominee.rowId) {
                            CheckableRow(
                                state = CheckableRowState(
                                    rowId = nominee.rowId,
                                    title = nominee.title,
                                    isChecked = nominee.isChecked,
                                    onCheckedChange = nominee.onCheckedChange,
                                    image = nominee.image,
                                    endIcon = nominee.endIcon
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Immutable
data class ExpandableCardState(
    val title: String,
    val nomineeStates: List<CheckableRowState>
)

@Immutable
data class ExpandableCardAccessibilityState(
    val seasonYear: String
)