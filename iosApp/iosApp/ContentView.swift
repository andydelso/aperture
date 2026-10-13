import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var vm = ContentViewModel()

    var body: some View {
        content
            .navigationTitle("Aperture")
    }

    @ViewBuilder
    private var content: some View {
        if let ready = vm.state as? MainScreenState.Ready {
            SeasonList(seasons: ready.seasons, onToggleWatched: vm.toggleWatched)
        } else if let error = vm.state as? MainScreenState.Error {
            Text(error.message)
                .foregroundColor(.secondary)
        } else {
            ProgressView()
        }
    }
}

/// One section per awards season, each category expandable like Android's ExpandableCard
struct SeasonList: View {
    let seasons: [SeasonUiModel]
    let onToggleWatched: (NomineeUiModel) -> Void

    var body: some View {
        List {
            ForEach(seasons, id: \.year) { season in
                Section(header: Text(season.year).font(.title2).bold()) {
                    ForEach(season.categories, id: \.title) { category in
                        DisclosureGroup(category.title) {
                            ForEach(category.nominees, id: \.id) { nominee in
                                NomineeRow(nominee: nominee) {
                                    onToggleWatched(nominee)
                                }
                            }
                        }
                    }
                }
            }
        }
        .listStyle(InsetGroupedListStyle())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
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
                onToggleWatched: { _ in }
            )
            .navigationTitle("Aperture")
        }
    }
}
