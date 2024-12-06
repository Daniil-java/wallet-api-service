--liquibase formatted sql

--changeset DanielK:1

CREATE TABLE IF NOT EXISTS wallets
(
    walletId    uuid PRIMARY KEY,
    balance     decimal NOT NULL,
    updated     timestamp NOT NULL,
    created     timestamp NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS transactions
(
    transactionId   uuid PRIMARY KEY,
    walletId        uuid NOT NULL REFERENCES wallets(walletId),
    operationType   varchar NOT NULL,
    amount          decimal NOT NULL,
    timestamp       timestamp NOT NULL DEFAULT current_timestamp,
    status          varchar NOT NULL,
    errorMessage    text
);
