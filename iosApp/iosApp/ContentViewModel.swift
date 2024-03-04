//
//  ContentViewModel.swift
//  iosApp
//
//  Created by Giovanni Noa on 2/7/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

class ContentViewModel: ObservableObject {
    @Published var items: [SelectAllAwardNominees]

    init() {
        items = KoinHelper().repository.getAllAwardNominees()
    }
}
