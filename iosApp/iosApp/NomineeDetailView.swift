import SwiftUI
import Observation
import Shared

/// Placeholder details for a nominee; shows its title for now
struct NomineeDetailView: View {
    @State private var model: NomineeDetailModel

    init(nomineeId: Int64) {
        _model = State(initialValue: NomineeDetailModel(nomineeId: nomineeId))
    }

    var body: some View {
        Group {
            if let ready = model.state as? NomineeDetailState.Ready {
                Text(ready.title)
                    .font(.largeTitle)
                    .bold()
                    .multilineTextAlignment(.center)
                    .padding()
            } else if model.state is NomineeDetailState.NotFound {
                ContentUnavailableView("Nominee not found", systemImage: "questionmark.circle")
            } else {
                ProgressView()
            }
        }
        .navigationBarTitleDisplayMode(.inline)
    }
}

/// Bridges the shared NomineeDetailViewModel to SwiftUI and clears it when this model goes away
@Observable
final class NomineeDetailModel {
    private(set) var state: NomineeDetailState = NomineeDetailState.Loading.shared

    @ObservationIgnored private let store = SwiftViewModelStore()
    @ObservationIgnored private var subscription: UiStateSubscription?

    init(nomineeId: Int64) {
        let viewModel = store.nomineeDetailViewModel(nomineeId: nomineeId)
        subscription = viewModel.observeUiState { [weak self] state in
            self?.state = state
        }
    }

    deinit {
        subscription?.close()
        store.clear()
    }
}

/// Shown in the detail column before a nominee is selected (iPad and other regular widths)
struct NomineeDetailPlaceholder: View {
    var body: some View {
        ContentUnavailableView(
            "Select a nominee",
            systemImage: "film",
            description: Text("Choose a nominee to see its details.")
        )
    }
}
