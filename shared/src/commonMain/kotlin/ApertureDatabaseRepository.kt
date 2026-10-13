import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.ddaypunk.aperture.db.ApertureDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Repository leveraging the platform provided database instance
 * Wraps queries with kotlin functions for ease of use
 */
class ApertureDatabaseRepository : KoinComponent {
    private val database: ApertureDatabase by inject()
    private val nominees = database.nomineeQueries
    private val awards = database.awardNomineeQueries

    fun getAllAwardNominees() = awards.selectAllAwardNominees().executeAsList()
    fun getAllAwardNomineesFlow() =
        awards.selectAllAwardNominees().asFlow().mapToList(Dispatchers.IO)

    fun getNomineeFlow(id: Long) =
        nominees.selectNomineeById(id).asFlow().mapToOneOrNull(Dispatchers.IO)

    fun updateNomineeWatched(id: Long, isWatched: Boolean) {
        nominees.updateNomineeWatched(id = id, watched = isWatched)
    }
}