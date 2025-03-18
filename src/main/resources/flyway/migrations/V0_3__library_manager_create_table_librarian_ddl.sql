CREATE TABLE IF NOT EXISTS librarian(
    librarian_id    SERIAL      NOT NULL,
    user_id         INTEGER     NOT NULL,
    role            VARCHAR(32) NOT NULL,
    hire_date       DATE    NOT NULL,
    PRIMARY KEY (librarian_id),
    UNIQUE (user_id),
    CONSTRAINT fk_librarian_users
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
);