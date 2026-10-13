package com.ddaypunk.aperture.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ddaypunk.aperture.presentation.MainScreenViewModel
import com.ddaypunk.aperture.presentation.NomineeDetailViewModel
import com.ddaypunk.aperture.screen.MainScreen
import com.ddaypunk.aperture.screen.NomineeDetailPlaceholder
import com.ddaypunk.aperture.screen.NomineeDetailScreen

/**
 * Renders the shared [Navigator] back stack with Navigation 3. On wide windows the list-detail
 * strategy shows the nominee list and details side by side; on phones they stack.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ApertureNavDisplay(
    navigator: Navigator,
    mainScreenViewModel: MainScreenViewModel
) {
    val backStack by navigator.backStack.collectAsState()
    val listDetailStrategy = rememberListDetailSceneStrategy<Route>()

    NavDisplay(
        backStack = backStack,
        // system back and predictive back go through the shared navigator
        onBack = { navigator.pop() },
        sceneStrategies = listOf(listDetailStrategy),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // scopes each entry's ViewModels to that entry, so each nominee gets its own details VM
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Main>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = { NomineeDetailPlaceholder() }
                )
            ) {
                MainScreen(mainScreenViewModel)
            }
            entry<Route.NomineeDetail>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { route ->
                NomineeDetailScreen(
                    viewModel = viewModel { NomineeDetailViewModel(route.nomineeId) }
                )
            }
        }
    )
}
