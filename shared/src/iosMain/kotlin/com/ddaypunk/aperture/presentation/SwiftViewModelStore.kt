package com.ddaypunk.aperture.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

/**
 * Owns view models created from Swift. SwiftUI has no ViewModelStore, so without this a Kotlin
 * ViewModel's viewModelScope would never be cancelled. Call [clear] when the Swift owner goes away.
 */
class SwiftViewModelStore : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()

    fun nomineeDetailViewModel(nomineeId: Long): NomineeDetailViewModel =
        ViewModelProvider.create(
            owner = this,
            factory = viewModelFactory { initializer { NomineeDetailViewModel(nomineeId) } }
        )[NomineeDetailViewModel::class]

    fun clear() {
        viewModelStore.clear()
    }
}
