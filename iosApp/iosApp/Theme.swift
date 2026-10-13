import SwiftUI

/// Oscars brand colors from the asset catalog; light/dark variants live in the color sets.
/// Values match the Android theme (composeApp theme/Color.kt).
/// On iOS the brand shows through accents (red watched state via AccentColor, gold winner badge)
/// while the navigation bar keeps the system style.
extension Color {
    static let oscarsRed = Color("OscarsRed")
    static let oscarsLightGold = Color("OscarsLightGold")
    static let winnerBadge = Color("WinnerBadge")
    static let onWinnerBadge = Color("OnWinnerBadge")
}
