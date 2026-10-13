import SwiftUI
import Shared

/// A nominee with a watched toggle and an optional winner badge, matching Android's CheckableRow
struct NomineeRow: View {
    let nominee: NomineeUiModel
    let onToggleWatched: () -> Void

    var body: some View {
        Button(action: onToggleWatched) {
            HStack(spacing: 12) {
                Image(systemName: nominee.isWatched ? "checkmark.circle.fill" : "circle")
                    .imageScale(.large)
                    .foregroundStyle(nominee.isWatched ? Color.accentColor : Color.secondary)
                Text(nominee.title)
                    .foregroundStyle(.primary)
                Spacer()
                if nominee.isWinner {
                    WinnerBadge()
                }
            }
        }
        .accessibilityLabel(nominee.title)
        .accessibilityValue(nominee.isWatched ? "Watched" : "Not watched")
        .accessibilityHint(nominee.isWinner ? "Category winner" : "")
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
            .accessibilityHidden(true)
    }
}
