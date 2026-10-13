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
        switch vm.state {
        case .ready(let seasons):
            SeasonList(
                seasons: seasons,
                selection: Binding(
                    get: { navigation.selectedNomineeId },
                    set: { navigation.selectedNomineeId = $0 }
                ),
                onToggleWatched: vm.toggleWatched
            )
        case .error(let message):
            ContentUnavailableView(
                "Couldn't load nominees",
                systemImage: "exclamationmark.triangle",
                description: Text(message)
            )
        case .loading:
            ProgressView()
        }
    }
}

/// One section per awards season, each category expandable like Android's ExpandableCard
struct SeasonList: View {
    let seasons: [SeasonItem]
    @Binding var selection: Int64?
    let onToggleWatched: (NomineeItem) -> Void

    var body: some View {
        List(selection: $selection) {
            ForEach(seasons) { season in
                Section {
                    ForEach(season.categories) { category in
                        DisclosureGroup(category.title) {
                            ForEach(category.nominees) { nominee in
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
                SeasonItem(
                    year: "2024",
                    categories: [
                        CategoryItem(
                            title: "Best Previews Ever",
                            nominees: [
                                NomineeItem(id: 1, title: "Star Wars: The Force Awakens", isWatched: true, isWinner: true),
                                NomineeItem(id: 2, title: "Harry Potter and the Order of the Phoenix", isWatched: false, isWinner: false),
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
