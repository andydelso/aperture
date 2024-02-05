package com.ddaypunk.aperture.data

data class Nominee(
    val id: Long,
    val name: String,
    val secondary: List<String>? = null,
    val won: Boolean = false,
    val note: String? = null,
    val link: String? = null,
    val type: NomineeType? = null
)

enum class NomineeType {
    FILM,
    PERSONNEL
}

enum class NomineePosition {
    PRIMARY,
    SECONDARY
}
