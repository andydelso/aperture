import app.cash.sqldelight.db.SqlDriver
import com.ddaypunk.aperture.db.ApertureDatabase

/**
 * KMM expectation for platforms to implement their specific driver creation factories given the following:
 * - Database schema: [ApertureDatabase] (declared in :shared/build.gradle.kts sqldelight block) and *.sq files found in :shared/src/common/sqldelight
 * - in Android returns an AndroidSqliteDriver instance - see :shared/src/androidMain/DriverFactory.android.kt
 * - in iOS returns a NativeSqliteDriver instance - see :shared/src/iosMain/DriverFactory.ios.kt
 */
expect class DriverFactory {
    fun createDriver(): SqlDriver
}

/**
 * KMM function to create the sqlite database using sqldelight library
 * @param driverFactory [DriverFactory] determined by client running the app currently via kmm internals
 * @return an instance of the database based on current platform
 */
fun createDatabase(driverFactory: DriverFactory): ApertureDatabase {
    return ApertureDatabase(
        driverFactory.createDriver()
    )
}
