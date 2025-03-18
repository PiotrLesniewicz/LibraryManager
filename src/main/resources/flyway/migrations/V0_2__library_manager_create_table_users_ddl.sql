CREATE TABLE IF NOT EXISTS users(
    user_id         SERIAL          NOT NULL,
    user_name       VARCHAR(16)     NOT NULL,
    name            VARCHAR(16)     NOT NULL,
    surname         VARCHAR(32)     NOT NULL,
    email           VARCHAR(32)     NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    phone_number    VARCHAR(20),
    membership_date DATE            NOT NULL,
    role            VARCHAR(32)     NOT NULL,
    address_id      INTEGER         NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE  (user_name),
    UNIQUE  (email),
    CONSTRAINT fk_users_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);