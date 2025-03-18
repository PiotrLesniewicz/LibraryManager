CREATE TABLE IF NOT EXISTS books(
    book_id     SERIAL      NOT NULL,
    title       VARCHAR(64) NOT NULL,
    isbn        VARCHAR(14),
    PRIMARY KEY (book_id)
)