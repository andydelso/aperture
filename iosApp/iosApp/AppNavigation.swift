import Foundation
import Observation
import Shared

/// Bridges the shared Kotlin Navigator to SwiftUI. The shared back stack is the source of truth;
/// NavigationSplitView reads the selected nominee from it and writes user/system navigation back.
@Observable
final class AppNavigation {
    private(set) var backStack: [Route] = [Route.Main.shared]

    @ObservationIgnored private let navigator = KoinHelper().navigator
    @ObservationIgnored private var subscription: UiStateSubscription?

    init() {
        subscription = navigator.observeBackStack { [weak self] backStack in
            self?.backStack = backStack
        }
    }

    deinit {
        subscription?.close()
    }

    /// The nominee whose details are on top of the shared back stack, if any.
    /// Setting it opens that nominee's details; setting nil (e.g. swipe back on iPhone) pops to the list.
    var selectedNomineeId: Int64? {
        get { (backStack.last as? Route.NomineeDetail)?.nomineeId }
        set {
            if let nomineeId = newValue {
                navigator.openNomineeDetail(nomineeId: nomineeId)
            } else {
                navigator.popToRoot()
            }
        }
    }
}
