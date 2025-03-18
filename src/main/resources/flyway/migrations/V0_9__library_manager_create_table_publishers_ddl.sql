CREATE TABLE IF NOT EXISTS publishers(
    publisher_id        SERIAL      NOT NULL,
    name                VARCHAR(64) NOT NULL,
    website             VARCHAR(255),
    PRIMARY KEY (publisher_id),
    UNIQUE (name)
);