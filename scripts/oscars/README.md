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

The upstream CSV is pinned by commit in `UPSTREAM_COMMIT`; bump it after a new ceremony lands
upstream. Pass `--csv path/to/oscars.csv` to use a local copy instead of downloading.

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
