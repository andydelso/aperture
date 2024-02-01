import app.cash.sqldelight.db.SqlDriver
import com.ddaypunk.aperture.db.ApertureDatabase

/**
 * expect classes and interfaces is in beta and expected to change in the future but I am trying to
 * follow the tutorial for the time being so a compiler argument ws added in the shared build file
 */
expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ApertureDatabase {
    return ApertureDatabase(
        driverFactory.createDriver()
    )
}
