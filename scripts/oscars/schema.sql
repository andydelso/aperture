-- Seed database schema written by build_seed.py.
-- Mirrors the planned SQLDelight .sq files so a prebuilt aperture.db can be shipped with the app.

CREATE TABLE ceremony (
    number INTEGER PRIMARY KEY,      -- 1 for the 1929 ceremony
    filmYear TEXT NOT NULL,          -- year(s) of the honored films, e.g. "1927/28" or "2025"
    ceremonyYear INTEGER NOT NULL    -- year the ceremony was held
);

CREATE TABLE category (
    id INTEGER PRIMARY KEY,
    canonicalName TEXT UNIQUE NOT NULL, -- stable across renames, e.g. "BEST PICTURE"
    awardClass TEXT NOT NULL            -- Title, Acting, Directing, Writing, Music, Production, SciTech, Special
);

CREATE TABLE film (
    id INTEGER PRIMARY KEY,
    imdbId TEXT UNIQUE NOT NULL,
    title TEXT NOT NULL,
    tmdbId INTEGER,                     -- filled in later from TMDB's /find endpoint
    watched INTEGER DEFAULT 0 NOT NULL  -- per film, so it shows across every category the film is in
);

CREATE TABLE nomination (
    id INTEGER PRIMARY KEY,
    ceremony INTEGER NOT NULL REFERENCES ceremony(number),
    categoryId INTEGER NOT NULL REFERENCES category(id),
    categoryName TEXT NOT NULL,      -- the official wording that year, e.g. "OUTSTANDING PICTURE"
    name TEXT NOT NULL,              -- official nominee text, e.g. "Emma Thomas, Charles Roven and Christopher Nolan, Producers"
    nominees TEXT NOT NULL,          -- '|' separated names
    nomineeIds TEXT NOT NULL,        -- '|' separated IMDb name/company ids, '?' when unknown
    won INTEGER DEFAULT 0 NOT NULL,
    detail TEXT NOT NULL,            -- '|' separated, e.g. character names or song titles
    note TEXT
);

-- Usually one film per nomination; early acting and writing nominations can cover several
CREATE TABLE nominationFilm (
    nominationId INTEGER NOT NULL REFERENCES nomination(id),
    filmId INTEGER NOT NULL REFERENCES film(id),
    position INTEGER NOT NULL,
    PRIMARY KEY (nominationId, filmId)
);

CREATE INDEX nomination_ceremony_category ON nomination(ceremony, categoryId);
CREATE INDEX nominationFilm_film ON nominationFilm(filmId);
