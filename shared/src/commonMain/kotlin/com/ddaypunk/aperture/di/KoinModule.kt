package com.ddaypunk.aperture.di

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