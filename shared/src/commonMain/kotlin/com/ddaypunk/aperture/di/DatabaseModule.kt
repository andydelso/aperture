package com.ddaypunk.aperture.di

import com.ddaypunk.aperture.db.ApertureDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        ApertureDatabase(
            get()
        )
    }
}