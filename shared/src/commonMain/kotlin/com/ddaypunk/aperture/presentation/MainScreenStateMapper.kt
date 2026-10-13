package com.ddaypunk.aperture.presentation

import com.ddaypunk.aperture.db.SelectNominationsByCategory

/**
 * Groups the flat nomination rows from the database into seasons and categories
 * @return seasons newest ceremony first, each with categories named as they were that year and
 * nominees sorted winners first, then by title
 */
internal fun List<SelectNominationsByCategory>.toSeasonUiModels(): List<SeasonUiModel> =
    groupBy { it.ceremony }
        .toList()
        .sortedByDescending { (ceremony, _) -> ceremony }
        .map { (ceremony, rows) ->
            SeasonUiModel(
                year = "${rows.first().ceremonyYear} · ${ceremony.toOrdinal()}",
                categories = rows.groupBy { it.categoryName }
                    .map { (categoryName, nominees) ->
                        CategoryUiModel(
                            title = categoryName,
                            nominees = nominees
                                .sortedWith(compareBy({ !it.won }, { it.title }))
                                .map { it.toNomineeUiModel() }
                        )
                    }
            )
        }

private fun SelectNominationsByCategory.toNomineeUiModel() = NomineeUiModel(
    id = filmId,
    title = title,
    isWatched = watched,
    isWinner = won
)

/** 1 -> "1st", 2 -> "2nd", 11 -> "11th", 98 -> "98th" */
internal fun Long.toOrdinal(): String {
    val suffix = when {
        this % 100 in 11..13 -> "th"
        this % 10 == 1L -> "st"
        this % 10 == 2L -> "nd"
        this % 10 == 3L -> "rd"
        else -> "th"
    }
    return "$this$suffix"
}
