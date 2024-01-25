import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ddaypunk.aperture.db.ApertureDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(ApertureDatabase.Schema, context, "test.db")
}