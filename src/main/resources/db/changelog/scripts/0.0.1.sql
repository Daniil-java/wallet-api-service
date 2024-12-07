--liquibase formatted sql

--changeset DanielK:1

CREATE TABLE IF NOT EXISTS wallets
(
    wallet_id    uuid PRIMARY KEY,
    balance      decimal NOT NULL,
    updated      timestamp NOT NULL,
    created      timestamp NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS transactions
(
    transaction_id   uuid PRIMARY KEY,
    wallet_id        uuid NOT NULL REFERENCES wallets(wallet_id),
    operation_type   varchar NOT NULL,
    amount           decimal NOT NULL,
    timestamp        timestamp NOT NULL DEFAULT current_timestamp,
    status           varchar NOT NULL,
    error_message    text
);
