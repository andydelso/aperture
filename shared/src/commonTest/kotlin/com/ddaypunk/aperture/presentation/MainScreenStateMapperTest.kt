package com.ddaypunk.aperture.presentation

import com.ddaypunk.aperture.db.SelectNominationsByCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainScreenStateMapperTest {

    private fun row(
        filmId: Long,
        title: String,
        ceremony: Long = 98,
        ceremonyYear: Long = 2026,
        categoryName: String = "BEST PICTURE",
        watched: Boolean = false,
        won: Boolean = false
    ) = SelectNominationsByCategory(
        ceremony = ceremony,
        ceremonyYear = ceremonyYear,
        filmYear = (ceremonyYear - 1).toString(),
        categoryName = categoryName,
        filmId = filmId,
        title = title,
        watched = watched,
        won = won,
        name = "Producers"
    )

    @Test
    fun emptyRowsMapToNoSeasons() {
        assertTrue(emptyList<SelectNominationsByCategory>().toSeasonUiModels().isEmpty())
    }

    @Test
    fun seasonsAreLabeledWithCeremonyYearAndOrdinal() {
        val seasons = listOf(
            row(filmId = 1, title = "A", ceremony = 98, ceremonyYear = 2026),
            row(filmId = 2, title = "B", ceremony = 1, ceremonyYear = 1929),
        ).toSeasonUiModels()

        assertEquals(listOf("2026 · 98th", "1929 · 1st"), seasons.map { it.year })
    }

    @Test
    fun newestCeremonyComesFirst() {
        val seasons = listOf(
            row(filmId = 1, title = "A", ceremony = 96, ceremonyYear = 2024),
            row(filmId = 2, title = "B", ceremony = 98, ceremonyYear = 2026),
            row(filmId = 3, title = "C", ceremony = 97, ceremonyYear = 2025),
        ).toSeasonUiModels()

        assertEquals(listOf("2026 · 98th", "2025 · 97th", "2024 · 96th"), seasons.map { it.year })
    }

    @Test
    fun nomineesAreSortedWinnersFirstThenByTitle() {
        val nominees = listOf(
            row(filmId = 1, title = "Sinners"),
            row(filmId = 2, title = "One Battle after Another", won = true),
            row(filmId = 3, title = "Bugonia"),
        ).toSeasonUiModels().single().categories.single().nominees

        assertEquals(listOf("One Battle after Another", "Bugonia", "Sinners"), nominees.map { it.title })
    }

    @Test
    fun categoryUsesThatYearsOfficialName() {
        val seasons = listOf(
            row(filmId = 1, title = "Anora", ceremony = 97, ceremonyYear = 2025),
            row(filmId = 2, title = "Wings", ceremony = 1, ceremonyYear = 1929, categoryName = "OUTSTANDING PICTURE"),
        ).toSeasonUiModels()

        assertEquals(listOf("BEST PICTURE"), seasons[0].categories.map { it.title })
        assertEquals(listOf("OUTSTANDING PICTURE"), seasons[1].categories.map { it.title })
    }

    @Test
    fun nomineeFieldsAreMappedFromTheFilm() {
        val nominee = listOf(row(filmId = 7, title = "Past Lives", watched = true, won = false))
            .toSeasonUiModels().single().categories.single().nominees.single()

        assertEquals(NomineeUiModel(id = 7, title = "Past Lives", isWatched = true, isWinner = false), nominee)
    }

    @Test
    fun ordinalsUseTheRightSuffix() {
        assertEquals(
            listOf("1st", "2nd", "3rd", "4th", "11th", "12th", "13th", "21st", "22nd", "23rd", "98th", "101st", "111th"),
            listOf(1L, 2, 3, 4, 11, 12, 13, 21, 22, 23, 98, 101, 111).map { it.toOrdinal() }
        )
    }
}
