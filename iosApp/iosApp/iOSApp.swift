import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinModuleKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            NavigationView {
                ContentView()
            }
        }
    }
}