# Oscar seed data

`build_seed.py` turns [DLu/oscar_data](https://github.com/DLu/oscar_data)'s curated `oscars.csv`
into Aperture's seed data. It only reads and normalizes the CSV (no scraping), using the CSV's
`CanonicalCategory` and `Class` columns so renamed categories (e.g. "OUTSTANDING PICTURE" →
"BEST PICTURE") group together, and IMDb `tt` ids so films can later be looked up on TMDB.

```sh
python3 build_seed.py --out aperture_seed.db                              # film categories (default)
python3 build_seed.py --out best_picture.json --category "BEST PICTURE"   # one category as JSON
python3 build_seed.py --out everything.db --all-classes                   # adds SciTech and Special awards
python3 -m unittest test_build_seed                                       # tests
```

The app ships the default output (every film category) and queries by `category.canonicalName`,
so the Best Picture focus is a query filter rather than missing data. Other categories can be
surfaced later without a new import.

Pass `--csv path/to/oscars.csv` to use a local copy instead of downloading.

## Updating for a new ceremony

The upstream CSV is pinned by commit in `UPSTREAM_COMMIT` so the seed is reproducible.
Upstream usually adds nominations in January and winners after the ceremony in March.

1. Check [DLu/oscar_data commits](https://github.com/DLu/oscar_data/commits/main) for the new
   ceremony (nominations alone have empty `Winner` values; wait for the winners commit to ship
   results).
2. Set `UPSTREAM_COMMIT` in `build_seed.py` to that commit's full SHA.
3. Run `python3 -m unittest test_build_seed`, then regenerate the seed and spot-check the new
   ceremony's Best Picture nominees and winner.
4. Update the size table above if it moved noticeably.
5. Ship it to existing installs with a SQLDelight migration that adds the new rows, not by
   replacing the database file: the user's `watched` flags live in `film`. Match films on
   `imdbId`, which is stable, rather than on the generated `id`, which follows CSV order.

## Output

Tables are described in [`schema.sql`](schema.sql): `ceremony`, `category`, `film`, `nomination`,
and `nominationFilm` (early nominations can cover several films). The SQLite output sets
`PRAGMA user_version` to the SQLDelight schema version so it can ship as a prebuilt database.

Sizes at the 98th ceremony (2025 films):

| Selection | Films | Nominations | .db | .json |
|---|---|---|---|---|
| `--category "BEST PICTURE"` | 621 | 621 | 184 KB | 193 KB |
| default (film classes) | 5,250 | 10,834 | 2.0 MB | 3.0 MB (500 KB gzipped) |
| `--all-classes` | 5,264 | 12,137 | 2.3 MB | |

## License

The source data is © 2022 David V. Lu!!, BSD 2-Clause. Its copyright notice and license must
ship with any app that bundles the generated data:
https://github.com/DLu/oscar_data/blob/main/LICENSE

In-app attribution is tracked in [#27](https://github.com/andydelso/aperture/issues/27). Category and
nomination data originally comes from the Academy's
[awards database](https://awardsdatabase.oscars.org/), and film ids from IMDb.
