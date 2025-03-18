CREATE TABLE IF NOT EXISTS books_categories(
    book_category_id    SERIAL  NOT NULL,
    book_id             INTEGER NOT NULL,
    category_id         INTEGER NOT NULL,
    PRIMARY KEY (book_category_id),
    CONSTRAINT fk_book_categories_books
        FOREIGN KEY (book_id)
            REFERENCES books (book_id),
    CONSTRAINT fk_book_categories_categories
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id)
);