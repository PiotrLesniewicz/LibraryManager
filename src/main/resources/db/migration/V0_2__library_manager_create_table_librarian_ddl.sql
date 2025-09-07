CREATE TABLE IF NOT EXISTS librarian(
    librarian_id    SERIAL      NOT NULL,
    role            VARCHAR(32) NOT NULL,
    hire_date       DATE    NOT NULL,
    PRIMARY KEY (librarian_id)
);