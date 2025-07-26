CREATE TABLE IF NOT EXISTS loan_penalty(
    loan_penalty_id SERIAL          NOT NULL,
    loan_id         INTEGER         NOT NULL,
    penalty_amount  NUMERIC(7,2)    NOT NULL,
    penalty_date    TIMESTAMP WITH TIME ZONE    NOT NULL,
    penalty_type    VARCHAR(32)     NOT NULL,
    PRIMARY KEY (loan_penalty_id),
    CONSTRAINT fk_loan_penalty_loan
        FOREIGN KEY (loan_id)
            REFERENCES loan (loan_id)
);