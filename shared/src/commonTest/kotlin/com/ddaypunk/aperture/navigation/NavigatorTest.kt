package com.ddaypunk.aperture.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigatorTest {

    @Test
    fun startsAtTheRoot() {
        assertEquals(listOf(Route.Main), Navigator().backStack.value)
    }

    @Test
    fun openingANomineePushesItsDetails() {
        val navigator = Navigator()

        navigator.openNomineeDetail(7)

        assertEquals(listOf(Route.Main, Route.NomineeDetail(7)), navigator.backStack.value)
    }

    @Test
    fun openingAnotherNomineeReplacesTheShownDetails() {
        val navigator = Navigator()

        navigator.openNomineeDetail(7)
        navigator.openNomineeDetail(9)

        assertEquals(listOf(Route.Main, Route.NomineeDetail(9)), navigator.backStack.value)
    }

    @Test
    fun popRemovesTheTopRoute() {
        val navigator = Navigator()
        navigator.openNomineeDetail(7)

        assertTrue(navigator.pop())
        assertEquals(listOf(Route.Main), navigator.backStack.value)
    }

    @Test
    fun popNeverRemovesTheRoot() {
        val navigator = Navigator()

        assertFalse(navigator.pop())
        assertEquals(listOf(Route.Main), navigator.backStack.value)
    }

    @Test
    fun popToRootClearsDetails() {
        val navigator = Navigator()
        navigator.openNomineeDetail(7)

        navigator.popToRoot()

        assertEquals(listOf(Route.Main), navigator.backStack.value)
    }
}
