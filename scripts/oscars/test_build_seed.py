import json
import sqlite3
import tempfile
import unittest
from pathlib import Path

from build_seed import build_seed, ceremony_year, write_json, write_sqlite, SCHEMA_VERSION

COLUMNS = ["Ceremony", "Year", "Class", "CanonicalCategory", "Category", "Film", "FilmId",
           "Name", "Nominees", "NomineeIds", "Winner", "Detail", "Note", "Citation"]


def row(**values):
    # csv.DictReader fills missing trailing columns with None, as upstream rows often end early
    return {column: values.get(column) for column in COLUMNS}


ROWS = [
    row(Ceremony="1", Year="1927/28", Class="Title", CanonicalCategory="BEST PICTURE",
        Category="OUTSTANDING PICTURE", Film="Wings", FilmId="tt0018578",
        Name="Paramount Famous Lasky", Nominees="Paramount Famous Lasky", NomineeIds="co0023400", Winner="True"),
    row(Ceremony="1", Year="1927/28", Class="Acting", CanonicalCategory="ACTOR IN A LEADING ROLE",
        Category="ACTOR", Film="The Noose|The Patent Leather Kid", FilmId="tt0019217|tt0018253",
        Name="Richard Barthelmess", Nominees="Richard Barthelmess", NomineeIds="nm0001932", Winner="",
        Detail="Nickie Elkins|The Patent Leather Kid"),
    row(Ceremony="96", Year="2023", Class="Title", CanonicalCategory="BEST PICTURE",
        Category="BEST PICTURE", Film="Oppenheimer", FilmId="tt15398776",
        Name="Emma Thomas, Charles Roven and Christopher Nolan, Producers",
        Nominees="Emma Thomas|Charles Roven|Christopher Nolan", NomineeIds="nm0858799|nm0746273|nm0634240",
        Winner="True"),
    row(Ceremony="96", Year="2023", Class="Directing", CanonicalCategory="DIRECTING",
        Category="DIRECTING", Film="Oppenheimer", FilmId="tt15398776",
        Name="Christopher Nolan", Nominees="Christopher Nolan", NomineeIds="nm0634240", Winner="True"),
    row(Ceremony="96", Year="2023", Class="Special", CanonicalCategory="HONORARY AWARD",
        Category="HONORARY AWARD", Name="Angela Bassett", Nominees="Angela Bassett", NomineeIds="nm0000291",
        Winner="True"),
]


class CeremonyYearTest(unittest.TestCase):
    def test_split_years_use_the_later_year(self):
        self.assertEqual(ceremony_year("1927/28"), 1929)
        self.assertEqual(ceremony_year("1932/33"), 1934)

    def test_single_year_is_the_following_year(self):
        self.assertEqual(ceremony_year("2025"), 2026)


class BuildSeedTest(unittest.TestCase):
    def test_default_style_class_filter_drops_special_awards(self):
        seed = build_seed(ROWS, classes={"Title", "Acting", "Directing"}, categories=None)

        self.assertEqual([n.name for n in seed.nominations if "Bassett" in n.name], [])
        self.assertEqual(len(seed.nominations), 4)

    def test_category_filter_keeps_only_best_picture_and_maps_old_names_to_canonical(self):
        seed = build_seed(ROWS, classes=None, categories={"BEST PICTURE"})

        self.assertEqual([c.canonicalName for c in seed.categories], ["BEST PICTURE"])
        self.assertEqual([n.categoryName for n in seed.nominations], ["OUTSTANDING PICTURE", "BEST PICTURE"])
        self.assertEqual({n.categoryId for n in seed.nominations}, {seed.categories[0].id})

    def test_film_shared_across_categories_is_stored_once(self):
        seed = build_seed(ROWS, classes=None, categories=None)

        oppenheimer = [f for f in seed.films if f.imdbId == "tt15398776"]
        self.assertEqual(len(oppenheimer), 1)
        linked = [n for n in seed.nominations if oppenheimer[0].id in n.filmIds]
        self.assertEqual(len(linked), 2)

    def test_multi_film_nomination_keeps_every_film_and_detail(self):
        seed = build_seed(ROWS, classes={"Acting"}, categories=None)

        nomination = seed.nominations[0]
        self.assertEqual([seed.films[i - 1].title for i in nomination.filmIds], ["The Noose", "The Patent Leather Kid"])
        self.assertEqual(nomination.detail, ["Nickie Elkins", "The Patent Leather Kid"])

    def test_winner_is_true_only_for_explicit_true(self):
        seed = build_seed(ROWS, classes=None, categories=None)

        self.assertEqual([n.won for n in seed.nominations], [True, False, True, True, True])

    def test_nomination_without_film_has_no_film_ids(self):
        seed = build_seed(ROWS, classes={"Special"}, categories=None)

        self.assertEqual(seed.nominations[0].filmIds, [])
        self.assertEqual(seed.films, [])

    def test_ceremonies_carry_film_and_ceremony_years(self):
        seed = build_seed(ROWS, classes=None, categories=None)

        self.assertEqual([(c.number, c.filmYear, c.ceremonyYear) for c in seed.ceremonies],
                         [(1, "1927/28", 1929), (96, "2023", 2024)])


class WriterTest(unittest.TestCase):
    def setUp(self):
        self.seed = build_seed(ROWS, classes=None, categories=None)
        self.dir = tempfile.TemporaryDirectory()
        self.addCleanup(self.dir.cleanup)

    def test_sqlite_output_round_trips_and_sets_schema_version(self):
        out = Path(self.dir.name) / "seed.db"
        write_sqlite(self.seed, out)

        db = sqlite3.connect(out)
        self.addCleanup(db.close)
        self.assertEqual(db.execute("PRAGMA user_version").fetchone()[0], SCHEMA_VERSION)
        best_picture_winners = db.execute(
            """
            SELECT ceremony.ceremonyYear, film.title FROM nomination
            JOIN category ON category.id = nomination.categoryId
            JOIN ceremony ON ceremony.number = nomination.ceremony
            JOIN nominationFilm ON nominationFilm.nominationId = nomination.id
            JOIN film ON film.id = nominationFilm.filmId
            WHERE category.canonicalName = 'BEST PICTURE' AND nomination.won
            ORDER BY ceremony.number
            """
        ).fetchall()
        self.assertEqual(best_picture_winners, [(1929, "Wings"), (2024, "Oppenheimer")])
        self.assertEqual(db.execute("SELECT COUNT(*) FROM nominationFilm").fetchone()[0], 5)

    def test_json_output_contains_every_table(self):
        out = Path(self.dir.name) / "seed.json"
        write_json(self.seed, out)

        data = json.loads(out.read_text())
        self.assertEqual(set(data), {"ceremonies", "categories", "films", "nominations"})
        self.assertEqual(len(data["nominations"]), len(ROWS))


if __name__ == "__main__":
    unittest.main()
