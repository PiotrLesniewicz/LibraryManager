CREATE TABLE IF NOT EXISTS address(
    address_id  SERIAL      NOT NULL,
    city        VARCHAR(32) NOT NULL,
    street      VARCHAR(32) NOT NULL,
    number      VARCHAR(8)  NOT NULL,
    post_code   VARCHAR(16) NOT NULL,
    PRIMARY KEY (address_id)
);