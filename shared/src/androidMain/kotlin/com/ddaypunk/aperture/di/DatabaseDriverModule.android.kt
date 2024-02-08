package com.ddaypunk.aperture.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase
import org.koin.dsl.module

actual val databaseDriverModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            ApertureDatabase.Schema,
            get(),
            "aperture.db"
        )
    }
}