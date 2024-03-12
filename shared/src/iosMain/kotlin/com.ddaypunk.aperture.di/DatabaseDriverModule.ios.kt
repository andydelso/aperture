package com.ddaypunk.aperture.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase
import org.koin.dsl.module

actual val databaseDriverModule = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            ApertureDatabase.Schema,
            "aperture.db"
        )
    }
}

