package com.ddaypunk.aperture

import data.Award
import data.Nominee
import data.Season

val mockData = listOf(
    Season(
        year = 2022,
        listOf(
            Award(
                category = "Best Motion Picture of the Year",
                nominations = listOf(
                    Nominee(
                        name = "CODA",
                        secondary = listOf(
                            "Philippe Rousselet",
                            "Fabrice Gianfermi",
                            "Patrick Wachsberger"
                        ),
                        won = true,
                        note = "CODA became the first movie produced by a streaming service to win Best Picture"
                    ),
                    Nominee(
                        name = "Belfast",
                        secondary = listOf(
                            "Laura Berwick",
                            "Kenneth Branagh",
                            "Becca Kovacik",
                            "Tamar Thomas"
                        )
                    ),
                    Nominee(
                        name = "Don't Look Up",
                        secondary = listOf(
                            "Adam McKay",
                            "Kevin J. Messick"
                        ),
                    ),
                    Nominee(
                        name = "Drive My Car",
                        secondary = listOf(
                            "Teruhisa Yamamoto"
                        ),
                    ),
                    Nominee(
                        name = "Dune",
                        secondary = listOf(
                            "Mary Parent",
                            "Denis Villeneuve",
                            "Cale Boyter"
                        ),
                    ),
                    Nominee(
                        name = "King Richard",
                        secondary = listOf(
                            "Tim White",
                            "Trevor White",
                            "Will Smith"
                        ),
                    ),
                    Nominee(
                        name = "Licorice Pizza",
                        secondary = listOf(
                            "Sara Murphy",
                            "Adam Somner",
                            "Paul Thomas Anderson"
                        ),
                    ),
                    Nominee(
                        name = "Nightmare Alley",
                        secondary = listOf(
                            "Guillermo del Toro",
                            "J. Miles Dale",
                            "Bradley Cooper"
                        ),
                    ),
                    Nominee(
                        name = "The Power of the Dog",
                        secondary = listOf(
                            "Jane Campion",
                            "Tanya Seghatchian",
                            "Emile Sherman",
                            "Iain Canning",
                            "Roger Frappier"
                        ),
                    ),
                    Nominee(
                        name = "West Side Story",
                        secondary = listOf(
                            "Steven Spielberg",
                            "Kristie Macosko Krieger"
                        ),
                    ),
                ),
            )
        )
    )
)