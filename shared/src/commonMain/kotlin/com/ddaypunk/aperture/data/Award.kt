package com.ddaypunk.aperture.data

data class Award(
    val id: Int? = null,
    val category: String,
    val nominations: List<Nominee>
)