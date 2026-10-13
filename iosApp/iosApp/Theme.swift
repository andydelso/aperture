import SwiftUI
import UIKit

/// Oscars brand colors from the asset catalog; light/dark variants live in the color sets.
/// Values match the Android theme (composeApp theme/Color.kt).
extension Color {
    static let oscarsRed = Color("OscarsRed")
    static let oscarsLightGold = Color("OscarsLightGold")
    static let winnerBadge = Color("WinnerBadge")
    static let onWinnerBadge = Color("OnWinnerBadge")
}

enum Theme {
    /// Red navigation bar with a light gold title, matching the Android top app bar.
    /// Uses UIKit appearance since toolbarBackground needs iOS 16 and the deployment target is 14.
    static func applyNavigationBarAppearance() {
        let gold = UIColor(named: "OscarsLightGold") ?? .white
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = UIColor(named: "OscarsRed")
        appearance.titleTextAttributes = [.foregroundColor: gold]
        appearance.largeTitleTextAttributes = [.foregroundColor: gold]

        let navigationBar = UINavigationBar.appearance()
        navigationBar.standardAppearance = appearance
        navigationBar.scrollEdgeAppearance = appearance
        navigationBar.compactAppearance = appearance
        navigationBar.tintColor = gold
    }
}
