CREATE TABLE IF NOT EXISTS book_item(
    book_item_id    SERIAL      NOT NULL,
    book_id         INTEGER     NOT NULL,
    barcode         VARCHAR(128)    NOT NULL,
    year_of_publication SMALLINT    NOT NULL,
    PRIMARY KEY (book_item_id),
    UNIQUE (barcode),
    CONSTRAINT fk_book_item_books
        FOREIGN KEY (book_id)
            REFERENCES books (book_id)
);