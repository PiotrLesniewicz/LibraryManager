CREATE TABLE IF NOT EXISTS loan(
    loan_id         SERIAL  NOT NULL,
    user_id         INTEGER NOT NULL,
    book_item_id    INTEGER NOT NULL,
    loan_date       TIMESTAMP WITH TIME ZONE    NOT NULL,
    return_date     TIMESTAMP WITH TIME ZONE    NOT NULL,
    status          VARCHAR(32) NOT NULL,
    PRIMARY KEY (loan_id),
    CONSTRAINT fk_loan_users
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_loan_book_item
        FOREIGN KEY (book_item_id)
            REFERENCES book_item (book_item_id)
);