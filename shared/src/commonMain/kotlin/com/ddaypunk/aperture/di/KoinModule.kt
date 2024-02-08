package com.ddaypunk.aperture.di

import ApertureDatabaseRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        repositoryModule,
        databaseDriverModule,
        databaseModule
    )
}

// for iOS
fun initKoin() = initKoin {}

class KoinHelper : KoinComponent {
    val repository: ApertureDatabaseRepository by inject()
}