import SwiftUI
import Shared

/// List-detail: on iPhone the details push over the list; on iPad they sit side by side
struct ContentView: View {
    @State private var vm = ContentViewModel()
    @State private var navigation = AppNavigation()

    var body: some View {
        NavigationSplitView {
            content
                .navigationTitle("Aperture")
        } detail: {
            if let nomineeId = navigation.selectedNomineeId {
                NomineeDetailView(nomineeId: nomineeId)
                    // new identity per nominee, so each gets its own detail model
                    .id(nomineeId)
            } else {
                NomineeDetailPlaceholder()
            }
        }
    }

    @ViewBuilder
    private var content: some View {
        if let ready = vm.state as? MainScreenState.Ready {
            SeasonList(
                seasons: ready.seasons,
                selection: Binding(
                    get: { navigation.selectedNomineeId },
                    set: { navigation.selectedNomineeId = $0 }
                ),
                onToggleWatched: vm.toggleWatched
            )
        } else if let error = vm.state as? MainScreenState.Error {
            ContentUnavailableView(
                "Couldn't load nominees",
                systemImage: "exclamationmark.triangle",
                description: Text(error.message)
            )
        } else {
            ProgressView()
        }
    }
}

/// One section per awards season, each category expandable like Android's ExpandableCard
struct SeasonList: View {
    let seasons: [SeasonUiModel]
    @Binding var selection: Int64?
    let onToggleWatched: (NomineeUiModel) -> Void

    var body: some View {
        List(selection: $selection) {
            ForEach(seasons, id: \.year) { season in
                Section {
                    ForEach(season.categories, id: \.title) { category in
                        DisclosureGroup(category.title) {
                            ForEach(category.nominees, id: \.id) { nominee in
                                NomineeRow(nominee: nominee) {
                                    onToggleWatched(nominee)
                                }
                                // keep normal colors when selected; the default prominent style
                                // turns content white and hides the red watched circle
                                .environment(\.backgroundProminence, .standard)
                                // tapping the row selects it, which opens its details
                                .tag(Int64?.some(nominee.id))
                                // subtle gold for the selected row instead of the system blue
                                .listRowBackground(
                                    selection == nominee.id ? Color.winnerBadge.opacity(0.3) : nil
                                )
                            }
                        }
                    }
                } header: {
                    Text(season.year)
                        .font(.title2)
                        .bold()
                }
            }
        }
        .listStyle(.insetGrouped)
    }
}

#Preview {
    NavigationStack {
        SeasonList(
            seasons: [
                SeasonUiModel(
                    year: "2024",
                    categories: [
                        CategoryUiModel(
                            title: "Best Previews Ever",
                            nominees: [
                                NomineeUiModel(id: 1, title: "Star Wars: The Force Awakens", isWatched: true, isWinner: true, image: nil),
                                NomineeUiModel(id: 2, title: "Harry Potter and the Order of the Phoenix", isWatched: false, isWinner: false, image: nil),
                            ]
                        )
                    ]
                )
            ],
            selection: .constant(nil),
            onToggleWatched: { _ in }
        )
        .navigationTitle("Aperture")
    }
}
