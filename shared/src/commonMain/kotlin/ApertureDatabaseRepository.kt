import com.ddaypunk.aperture.db.ApertureDatabase
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
    fun updateNomineeWatched(id: Long, isWatched: Boolean) {
        nominees.updateNomineeWatched(id = id, watched = isWatched)
    }
}