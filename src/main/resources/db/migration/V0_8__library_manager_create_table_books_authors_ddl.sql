CREATE TABLE IF NOT EXISTS books_authors(
    books_authors_id    SERIAL  NOT NULL,
    book_id             INTEGER NOT NULL,
    author_id           INTEGER NOT NULL,
    PRIMARY KEY (books_authors_id),
    CONSTRAINT fk_books_authors_books
        FOREIGN KEY (book_id)
            REFERENCES books (book_id),
    CONSTRAINT fk_books_authors_authors
        FOREIGN KEY (author_id)
            REFERENCES authors (author_id)
);