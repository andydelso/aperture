package com.ddaypunk.aperture.di

import ApertureDatabaseRepository
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val databaseDriverModule = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            ApertureDatabase.Schema,
            "aperture.db"
        )
    }
}

