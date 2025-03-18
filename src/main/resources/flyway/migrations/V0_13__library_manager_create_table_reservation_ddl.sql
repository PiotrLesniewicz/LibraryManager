CREATE TABLE IF NOT EXISTS reservation(
    reservation_id      SERIAL  NOT NULL,
    user_id             INTEGER NOT NULL,
    book_item_id        INTEGER NOT NULL,
    reservation_date    TIMESTAMP WITH TIME ZONE    NOT NULL,
    expiry_date         TIMESTAMP WITH TIME ZONE    NOT NULL,
    cancelled_date      TIMESTAMP WITH TIME ZONE,
    status              VARCHAR(32) NOT NULL,
    PRIMARY KEY (reservation_id),
    CONSTRAINT fk_reservation_users
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_reservation_book_item
        FOREIGN KEY (book_item_id)
            REFERENCES book_item (book_item_id)
);