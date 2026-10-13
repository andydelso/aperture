package com.ddaypunk.aperture.di

import com.ddaypunk.aperture.navigation.Navigator
import org.koin.dsl.module

// single: one back stack for the whole app, shared by view models and both platform UIs
val navigationModule = module {
    single { Navigator() }
}
