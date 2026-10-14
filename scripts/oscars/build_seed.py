#!/usr/bin/env python3
"""
Builds Aperture's Oscar seed data from DLu/oscar_data's oscars.csv.

The CSV is curated upstream (scraped from awardsdatabase.oscars.org and matched to IMDb ids),
so this script only reads and normalizes it - no scraping. It writes the same normalized
tables either as a SQLite database (matching schema.sql) or as JSON.

Usage:
    ./build_seed.py --out aperture_seed.db
    ./build_seed.py --out best_picture.json --category "BEST PICTURE"
    ./build_seed.py --csv path/to/oscars.csv --out aperture_seed.db --class Title --class Acting

Standard library only (Python 3.10+).
"""
import argparse
import csv
import io
import json
import sqlite3
import sys
import urllib.request
from dataclasses import dataclass, field, asdict
from pathlib import Path

# Pinned so the seed is reproducible; bump to pick up a new ceremony
UPSTREAM_COMMIT = "c5e9716b7e020e70205d6b95f5a5678526c1b45f"
UPSTREAM_CSV_URL = f"https://raw.githubusercontent.com/DLu/oscar_data/{UPSTREAM_COMMIT}/oscars.csv"

# Classes whose nominations are tied to films. SciTech and Special are mostly people/companies.
DEFAULT_CLASSES = ("Title", "Acting", "Directing", "Writing", "Music", "Production")

SCHEMA_PATH = Path(__file__).with_name("schema.sql")
SCHEMA_VERSION = 1


@dataclass
class Ceremony:
    number: int
    filmYear: str
    ceremonyYear: int


@dataclass
class Category:
    id: int
    canonicalName: str
    awardClass: str


@dataclass
class Film:
    id: int
    imdbId: str
    title: str


@dataclass
class Nomination:
    id: int
    ceremony: int
    categoryId: int
    categoryName: str
    name: str
    nominees: list[str]
    nomineeIds: list[str]
    won: bool
    detail: list[str]
    note: str | None
    filmIds: list[int] = field(default_factory=list)


@dataclass
class Seed:
    ceremonies: list[Ceremony]
    categories: list[Category]
    films: list[Film]
    nominations: list[Nomination]


def split_pipes(value: str | None) -> list[str]:
    """Upstream separates multiple values with '|' and uses '?' for unknown IMDb ids"""
    if not value:
        return []
    return [part.strip() for part in value.split("|")]


def ceremony_year(film_year: str) -> int:
    """
    The ceremony is held the year after the films it honors.
    Early years span two calendar years, e.g. "1927/28" -> 1929.
    """
    if "/" in film_year:
        start, end = film_year.split("/")
        end_year = int(start[:-len(end)] + end)
    else:
        end_year = int(film_year)
    return end_year + 1


def build_seed(rows: list[dict], classes: set[str] | None, categories: set[str] | None) -> Seed:
    ceremonies: dict[int, Ceremony] = {}
    category_ids: dict[str, Category] = {}
    films: dict[str, Film] = {}
    nominations: list[Nomination] = []

    for row in rows:
        award_class = row["Class"]
        canonical = row["CanonicalCategory"]
        if classes and award_class not in classes:
            continue
        if categories and canonical not in categories:
            continue

        number = int(row["Ceremony"])
        if number not in ceremonies:
            ceremonies[number] = Ceremony(number, row["Year"], ceremony_year(row["Year"]))

        if canonical not in category_ids:
            category_ids[canonical] = Category(len(category_ids) + 1, canonical, award_class)

        film_ids = []
        for imdb_id, title in zip(split_pipes(row["FilmId"]), split_pipes(row["Film"])):
            if not imdb_id.startswith("tt"):
                continue
            if imdb_id not in films:
                films[imdb_id] = Film(len(films) + 1, imdb_id, title)
            film_ids.append(films[imdb_id].id)

        nominations.append(
            Nomination(
                id=len(nominations) + 1,
                ceremony=number,
                categoryId=category_ids[canonical].id,
                categoryName=row["Category"],
                name=row["Name"] or "",
                nominees=split_pipes(row["Nominees"]),
                nomineeIds=split_pipes(row["NomineeIds"]),
                # Missing trailing columns read as None, so only an explicit "True" counts
                won=row.get("Winner") == "True",
                detail=split_pipes(row.get("Detail")),
                note=row.get("Note") or None,
                filmIds=film_ids,
            )
        )

    return Seed(
        ceremonies=sorted(ceremonies.values(), key=lambda c: c.number),
        categories=list(category_ids.values()),
        films=list(films.values()),
        nominations=nominations,
    )


def write_json(seed: Seed, out: Path) -> None:
    out.write_text(json.dumps(asdict(seed), ensure_ascii=False, separators=(",", ":")))


def write_sqlite(seed: Seed, out: Path) -> None:
    out.unlink(missing_ok=True)
    db = sqlite3.connect(out)
    try:
        db.executescript(SCHEMA_PATH.read_text())
        db.executemany(
            "INSERT INTO ceremony (number, filmYear, ceremonyYear) VALUES (?, ?, ?)",
            [(c.number, c.filmYear, c.ceremonyYear) for c in seed.ceremonies],
        )
        db.executemany(
            "INSERT INTO category (id, canonicalName, awardClass) VALUES (?, ?, ?)",
            [(c.id, c.canonicalName, c.awardClass) for c in seed.categories],
        )
        db.executemany(
            "INSERT INTO film (id, imdbId, title) VALUES (?, ?, ?)",
            [(f.id, f.imdbId, f.title) for f in seed.films],
        )
        db.executemany(
            "INSERT INTO nomination (id, ceremony, categoryId, categoryName, name, nominees, nomineeIds, won, detail, note)"
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            [
                (
                    n.id, n.ceremony, n.categoryId, n.categoryName, n.name,
                    "|".join(n.nominees), "|".join(n.nomineeIds), int(n.won),
                    "|".join(n.detail), n.note,
                )
                for n in seed.nominations
            ],
        )
        db.executemany(
            "INSERT INTO nominationFilm (nominationId, filmId, position) VALUES (?, ?, ?)",
            [(n.id, film_id, i) for n in seed.nominations for i, film_id in enumerate(n.filmIds)],
        )
        # SQLDelight compares this with Schema.version to decide whether to create or migrate
        db.execute(f"PRAGMA user_version = {SCHEMA_VERSION}")
        db.commit()
        db.execute("VACUUM")
    finally:
        db.close()


def read_rows(csv_path: Path | None) -> list[dict]:
    if csv_path:
        text = csv_path.read_text(encoding="utf-8")
    else:
        with urllib.request.urlopen(UPSTREAM_CSV_URL) as response:
            text = response.read().decode("utf-8")
    # Despite the extension, upstream oscars.csv is tab separated
    return list(csv.DictReader(io.StringIO(text), delimiter="\t"))


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--csv", type=Path, help="local oscars.csv; downloads the pinned upstream copy when omitted")
    parser.add_argument("--out", type=Path, required=True, help="output path ending in .db or .json")
    parser.add_argument("--class", dest="classes", action="append",
                        help=f"award class to include, repeatable (default: {', '.join(DEFAULT_CLASSES)})")
    parser.add_argument("--category", dest="categories", action="append",
                        help='canonical category to include, repeatable, e.g. "BEST PICTURE"')
    parser.add_argument("--all-classes", action="store_true", help="include SciTech and Special awards too")
    args = parser.parse_args(argv)

    classes = None if args.all_classes else set(args.classes or DEFAULT_CLASSES)
    categories = set(args.categories) if args.categories else None
    seed = build_seed(read_rows(args.csv), classes, categories)

    if args.out.suffix == ".json":
        write_json(seed, args.out)
    elif args.out.suffix == ".db":
        write_sqlite(seed, args.out)
    else:
        parser.error("--out must end in .db or .json")

    print(
        f"{len(seed.ceremonies)} ceremonies, {len(seed.categories)} categories, "
        f"{len(seed.films)} films, {len(seed.nominations)} nominations "
        f"-> {args.out} ({args.out.stat().st_size / 1024:.0f} KB)"
    )
    return 0


if __name__ == "__main__":
    sys.exit(main())
