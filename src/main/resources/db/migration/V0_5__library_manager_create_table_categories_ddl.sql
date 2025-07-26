CREATE TABLE IF NOT EXISTS categories(
    category_id SERIAL      NOT NULL,
    name        VARCHAR(32) NOT NULL,
    PRIMARY KEY (category_id),
    UNIQUE (name)
);