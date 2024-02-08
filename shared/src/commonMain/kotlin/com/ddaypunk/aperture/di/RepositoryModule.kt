package com.ddaypunk.aperture.di

import ApertureDatabaseRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { ApertureDatabaseRepository() }
}