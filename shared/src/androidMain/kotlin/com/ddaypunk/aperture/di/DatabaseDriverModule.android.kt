package com.ddaypunk.aperture.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase
import org.koin.dsl.module

private const val DATABASE_NAME = "aperture.db"

actual val databaseDriverModule = module {
    single<SqlDriver> {
        val context: Context = get()
        context.copyPrebuiltDatabaseIfMissing(DATABASE_NAME)
        AndroidSqliteDriver(
            ApertureDatabase.Schema,
            context,
            DATABASE_NAME
        )
    }
}

/**
 * Seeds the app's database from the prebuilt copy in assets on first launch.
 * Never overwrites an existing file, since it holds the user's watched flags.
 * Copies to a temp file first so an interrupted copy can't leave a partial database behind.
 */
private fun Context.copyPrebuiltDatabaseIfMissing(name: String) {
    val database = getDatabasePath(name)
    if (database.exists()) return

    database.parentFile?.mkdirs()
    val temp = database.resolveSibling("$name.tmp")
    assets.open(name).use { input ->
        temp.outputStream().use { output -> input.copyTo(output) }
    }
    check(temp.renameTo(database)) { "Couldn't move the prebuilt database into $database" }
}
