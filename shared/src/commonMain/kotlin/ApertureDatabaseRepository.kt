import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.ddaypunk.aperture.db.ApertureDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val BEST_PICTURE = "BEST PICTURE"

/**
 * Repository leveraging the platform provided database instance
 * Wraps queries with kotlin functions for ease of use
 */
class ApertureDatabaseRepository : KoinComponent {
    private val database: ApertureDatabase by inject()
    private val films = database.filmQueries
    private val nominations = database.nominationQueries

    fun getBestPictureNominationsFlow() =
        nominations.selectNominationsByCategory(canonicalName = BEST_PICTURE).asFlow().mapToList(Dispatchers.IO)

    fun getFilmFlow(id: Long) =
        films.selectFilmById(id).asFlow().mapToOneOrNull(Dispatchers.IO)

    fun updateFilmWatched(id: Long, isWatched: Boolean) {
        films.updateFilmWatched(watched = isWatched, id = id)
    }
}
