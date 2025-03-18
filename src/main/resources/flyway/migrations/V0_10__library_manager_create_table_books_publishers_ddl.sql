CREATE TABLE IF NOT EXISTS books_publishers(
    book_publisher_id   SERIAL  NOT NULL,
    book_id             INTEGER NOT NULL,
    publisher_id        INTEGER NOT NULL,
    PRIMARY KEY (book_publisher_id),
    CONSTRAINT fk_books_publishers_books
        FOREIGN KEY (book_id)
            REFERENCES books (book_id),
    CONSTRAINT fk_books_publishers_publishers
        FOREIGN KEY (publisher_id)
            REFERENCES publishers (publisher_id)
);