import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinModuleKt.doInitKoin()
        Theme.applyNavigationBarAppearance()
    }

    var body: some Scene {
        WindowGroup {
            NavigationView {
                ContentView()
            }
            .navigationViewStyle(StackNavigationViewStyle())
        }
    }
}
