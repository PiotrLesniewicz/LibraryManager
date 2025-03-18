CREATE TABLE IF NOT EXISTS opinions(
    opinion_id  SERIAL  NOT NULL,
    book_id     INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    rating      SMALLINT    NOT NULL    CHECK (rating BETWEEN 1 AND 5),
    comment     TEXT,
    create_date DATE    NOT NULL,
    PRIMARY KEY (opinion_id),
    CONSTRAINT fk_opinions_books
        FOREIGN KEY (book_id)
            REFERENCES books (book_id),
    CONSTRAINT fk_opinions_users
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
)