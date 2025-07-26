CREATE TABLE IF NOT EXISTS authors(
    author_id   SERIAL      NOT NULL,
    name        VARCHAR(32) NOT NULL,
    surname     VARCHAR(64),
    biography   TEXT,
    PRIMARY KEY (author_id)
)