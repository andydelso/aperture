package com.ddaypunk.aperture.presentation

import com.ddaypunk.aperture.db.SelectAllAwardNominees
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainScreenStateMapperTest {

    private fun row(
        id: Long,
        name: String,
        category: String = "Best Picture",
        year: Long = 2024,
        watched: Boolean = false,
        won: Boolean = false
    ) = SelectAllAwardNominees(
        categoryName = category,
        awardYear = year,
        id = id,
        name = name,
        type = "FILM",
        secondaryInfo = "",
        watched = watched,
        won = won,
        notes = null
    )

    @Test
    fun emptyRowsMapToNoSeasons() {
        assertTrue(emptyList<SelectAllAwardNominees>().toSeasonUiModels().isEmpty())
    }

    @Test
    fun rowsAreGroupedBySeasonThenCategory() {
        val seasons = listOf(
            row(id = 1, name = "A", category = "Best Picture", year = 2024),
            row(id = 2, name = "B", category = "Best Director", year = 2024),
            row(id = 3, name = "C", category = "Best Picture", year = 2023),
        ).toSeasonUiModels()

        assertEquals(listOf("2024", "2023"), seasons.map { it.year })
        assertEquals(listOf("Best Picture", "Best Director"), seasons[0].categories.map { it.title })
        assertEquals(listOf("Best Picture"), seasons[1].categories.map { it.title })
    }

    @Test
    fun nomineesAreSortedWinnersFirstThenByTitle() {
        val nominees = listOf(
            row(id = 1, name = "Zone of Interest"),
            row(id = 2, name = "Oppenheimer", won = true),
            row(id = 3, name = "Barbie"),
        ).toSeasonUiModels().single().categories.single().nominees

        assertEquals(listOf("Oppenheimer", "Barbie", "Zone of Interest"), nominees.map { it.title })
    }

    @Test
    fun nomineeFieldsAreMapped() {
        val nominee = listOf(row(id = 7, name = "Past Lives", watched = true, won = false))
            .toSeasonUiModels().single().categories.single().nominees.single()

        assertEquals(NomineeUiModel(id = 7, title = "Past Lives", isWatched = true, isWinner = false), nominee)
    }
}
