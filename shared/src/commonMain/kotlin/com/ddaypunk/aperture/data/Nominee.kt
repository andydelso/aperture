package com.ddaypunk.aperture.data

data class Nominee(
    val id: Int? = null,
    val name: String,
    val secondary: List<String>? = null,
    val won: Boolean = false,
    val note: String? = null,
    val link: String? = null,
    val type: NomineeType? = null
)

enum class NomineeType {
    FILM,
    SHORT,
    DOCUMENTARY,
    ACTOR,
    DIRECTOR,
    EDITOR
}
