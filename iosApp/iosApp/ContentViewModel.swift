//
//  ContentViewModel.swift
//  iosApp
//
//  Created by Giovanni Noa on 2/7/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Observation
import Shared

/// Bridges the shared Kotlin MainScreenViewModel to SwiftUI by republishing its state
@Observable
final class ContentViewModel {
    private(set) var state: MainScreenState = MainScreenState.Loading.shared

    @ObservationIgnored private let viewModel = MainScreenViewModel()
    @ObservationIgnored private var subscription: UiStateSubscription?

    init() {
        // called on the main thread with the current state and every update after it
        subscription = viewModel.observeUiState { [weak self] state in
            self?.state = state
        }
    }

    deinit {
        subscription?.close()
    }

    func toggleWatched(_ nominee: NomineeUiModel) {
        viewModel.onWatchedToggle(nomineeId: nominee.id, isWatched: !nominee.isWatched)
    }
}
