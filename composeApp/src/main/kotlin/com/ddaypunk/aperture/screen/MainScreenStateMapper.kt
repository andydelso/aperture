package com.ddaypunk.aperture.screen

import com.ddaypunk.aperture.component.CheckableRowState
import com.ddaypunk.aperture.component.ExpandableCardState
import com.ddaypunk.aperture.component.SectionHeaderState
import com.ddaypunk.aperture.presentation.CategoryUiModel
import com.ddaypunk.aperture.presentation.NomineeUiModel
import com.ddaypunk.aperture.presentation.SeasonUiModel

// Maps the shared UI models to this app's Compose component state wrappers

internal fun SeasonUiModel.toSectionHeaderState() = SectionHeaderState(title = year)

internal fun CategoryUiModel.toExpandableCardState(
    onWatchedToggle: (nomineeId: Long, isWatched: Boolean) -> Unit,
    onNomineeSelected: (nomineeId: Long) -> Unit
) = ExpandableCardState(
    title = title,
    nomineeStates = nominees.map { it.toCheckableRowState(onWatchedToggle, onNomineeSelected) }
)

private fun NomineeUiModel.toCheckableRowState(
    onWatchedToggle: (nomineeId: Long, isWatched: Boolean) -> Unit,
    onNomineeSelected: (nomineeId: Long) -> Unit
) = CheckableRowState(
    rowId = id,
    title = title,
    isChecked = isWatched,
    onCheckedChange = { onWatchedToggle(id, !isWatched) },
    onClick = { onNomineeSelected(id) },
    image = image,
    isWinner = isWinner
)
