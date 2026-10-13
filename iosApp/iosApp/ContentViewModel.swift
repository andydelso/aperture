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

/// Swift value types for the main screen. Kotlin objects are reference types, so SwiftUI would
/// treat every row as changed on each update; Equatable structs let it redraw only what changed.
enum ContentState: Equatable {
    case loading
    case ready([SeasonItem])
    case error(String)
}

struct SeasonItem: Identifiable, Equatable {
    let year: String
    let categories: [CategoryItem]
    var id: String { year }
}

struct CategoryItem: Identifiable, Equatable {
    let title: String
    let nominees: [NomineeItem]
    var id: String { title }
}

struct NomineeItem: Identifiable, Equatable {
    let id: Int64
    let title: String
    let isWatched: Bool
    let isWinner: Bool
}

/// Bridges the shared Kotlin MainScreenViewModel to SwiftUI by republishing its state as Swift values
@Observable
final class ContentViewModel {
    private(set) var state: ContentState = .loading

    @ObservationIgnored private let viewModel = MainScreenViewModel()
    @ObservationIgnored private var subscription: UiStateSubscription?

    init() {
        // called on the main thread with the current state and every update after it
        subscription = viewModel.observeUiState { [weak self] kotlinState in
            guard let self else { return }
            let newState = Self.map(kotlinState)
            // skip identical states so SwiftUI isn't invalidated for nothing
            if newState != self.state {
                self.state = newState
            }
        }
    }

    deinit {
        subscription?.close()
    }

    func toggleWatched(_ nominee: NomineeItem) {
        viewModel.onWatchedToggle(nomineeId: nominee.id, isWatched: !nominee.isWatched)
    }

    private static func map(_ state: MainScreenState) -> ContentState {
        if let ready = state as? MainScreenState.Ready {
            return .ready(ready.seasons.map { season in
                SeasonItem(
                    year: season.year,
                    categories: season.categories.map { category in
                        CategoryItem(
                            title: category.title,
                            nominees: category.nominees.map { nominee in
                                NomineeItem(
                                    id: nominee.id,
                                    title: nominee.title,
                                    isWatched: nominee.isWatched,
                                    isWinner: nominee.isWinner
                                )
                            }
                        )
                    }
                )
            })
        } else if let error = state as? MainScreenState.Error {
            return .error(error.message)
        }
        return .loading
    }
}
