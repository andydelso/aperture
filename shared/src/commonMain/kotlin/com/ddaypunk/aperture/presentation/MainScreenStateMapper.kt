package com.ddaypunk.aperture.presentation

import com.ddaypunk.aperture.db.SelectAllAwardNominees

/**
 * Groups the flat award nominee rows from the database into seasons and categories
 * @return seasons in query order, each with categories whose nominees are sorted winners first, then by title
 */
internal fun List<SelectAllAwardNominees>.toSeasonUiModels(): List<SeasonUiModel> =
    groupBy { it.awardYear }
        .map { (year, rows) ->
            SeasonUiModel(
                year = year.toString(),
                categories = rows.groupBy { it.categoryName }
                    .map { (categoryName, nominees) ->
                        CategoryUiModel(
                            title = categoryName,
                            nominees = nominees
                                .sortedWith(compareBy({ !it.won }, { it.name }))
                                .map { it.toNomineeUiModel() }
                        )
                    }
            )
        }

private fun SelectAllAwardNominees.toNomineeUiModel() = NomineeUiModel(
    id = id,
    title = name,
    isWatched = watched,
    isWinner = won
)
