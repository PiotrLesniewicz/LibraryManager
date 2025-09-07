CREATE TABLE IF NOT EXISTS users(
    user_id         SERIAL          NOT NULL,
    user_name       VARCHAR(32)     NOT NULL,
    name            VARCHAR(32)     NOT NULL,
    surname         VARCHAR(64)     NOT NULL,
    email           VARCHAR(64)     NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    phone_number    VARCHAR(20),
    membership_date DATE            NOT NULL,
    role            VARCHAR(32)     NOT NULL,
    address_id      INTEGER         NOT NULL,
    librarian_id    INTEGER,
    PRIMARY KEY (user_id),
    UNIQUE  (user_name),
    UNIQUE  (email),
    CONSTRAINT fk_users_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id),
    CONSTRAINT fk_users_librarian
        FOREIGN KEY (librarian_id)
            REFERENCES librarian (librarian_id)
);