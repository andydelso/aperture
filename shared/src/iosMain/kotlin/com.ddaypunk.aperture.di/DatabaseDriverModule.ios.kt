package com.ddaypunk.aperture.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

private const val DATABASE_NAME = "aperture.db"

actual val databaseDriverModule = module {
    single<SqlDriver> {
        copyPrebuiltDatabaseIfMissing(DATABASE_NAME)
        NativeSqliteDriver(
            ApertureDatabase.Schema,
            DATABASE_NAME
        )
    }
}

/**
 * Seeds the app's database from the prebuilt copy in the app bundle on first launch.
 * Never overwrites an existing file, since it holds the user's watched flags.
 * The destination is NativeSqliteDriver's default location: <Application Support>/databases/
 */
@OptIn(ExperimentalForeignApi::class)
private fun copyPrebuiltDatabaseIfMissing(name: String) {
    val fileManager = NSFileManager.defaultManager
    val applicationSupport = NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true)
        .first() as String
    val directory = "$applicationSupport/databases"
    val database = "$directory/$name"
    if (fileManager.fileExistsAtPath(database)) return

    val prebuilt = checkNotNull(
        NSBundle.mainBundle.pathForResource(name.substringBeforeLast('.'), ofType = name.substringAfterLast('.'))
    ) { "$name is missing from the app bundle" }
    check(fileManager.createDirectoryAtPath(directory, withIntermediateDirectories = true, attributes = null, error = null)) {
        "Couldn't create $directory"
    }
    check(fileManager.copyItemAtPath(prebuilt, toPath = database, error = null)) {
        "Couldn't copy the prebuilt database to $database"
    }
}
