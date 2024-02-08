import SwiftUI
import Shared

struct ContentView: View {
    @ObservedObject var vm = ContentViewModel()

    var body: some View {
        List {
            Section(header: Text("Best Motion Picture")) {
                    ForEach(0..<vm.items.count) { num in
                        VStack {
                            Text(vm.items[num].name)
                                .font(.callout)
                        }
                        //.foregroundColor(.lightGray)
                    }

            }
        }
                .navigationTitle("Title")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
