import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase


actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(
            ApertureDatabase.Schema,
            "test.db"
        )
}