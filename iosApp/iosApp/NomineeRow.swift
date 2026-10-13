import SwiftUI

/// A nominee with a watched toggle and an optional winner badge, matching Android's CheckableRow.
/// The circle toggles watched; tapping elsewhere on the row selects it (opening details).
struct NomineeRow: View {
    let nominee: NomineeItem
    let onToggleWatched: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            Button(action: onToggleWatched) {
                Image(systemName: nominee.isWatched ? "checkmark.circle.fill" : "circle")
                    .imageScale(.large)
                    .foregroundStyle(nominee.isWatched ? Color.accentColor : Color.secondary)
            }
            // borderless so only the circle handles the tap, not the whole list row
            .buttonStyle(.borderless)
            .accessibilityLabel(nominee.isWatched ? "Watched" : "Not watched")
            .accessibilityHint("Toggles watched")

            Text(nominee.title)
                // label color stays dark/light per appearance even in a selected row,
                // unlike the hierarchical .primary style which turns white there
                .foregroundStyle(Color(uiColor: .label))
            Spacer()
            if nominee.isWinner {
                WinnerBadge()
            }
        }
        .accessibilityElement(children: .contain)
    }
}

/// Gold capsule with a star, matching Android's winner Badge
struct WinnerBadge: View {
    var body: some View {
        Image(systemName: "star.fill")
            .font(.caption)
            .foregroundStyle(Color.onWinnerBadge)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(Capsule().fill(Color.winnerBadge))
            .accessibilityLabel("Category winner")
    }
}
