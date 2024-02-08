import SwiftUI
import Shared

struct ContentView: View {
    @ObservedObject var vm = ContentViewModel()

    var body: some View {

        Section(header: Text("Best Motion Picture")) {
            VStack {
                List(0..<vm.items.count) { num in
                    VStack {
                        Text(vm.items[num].name)
                                .font(.callout)
                        Text(vm.items[num].categoryName)
                                .font(.body)
                    }
                    //.foregroundColor(.lightGray)
                }
            }
        }
                .navigationTitle("Nominees")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
