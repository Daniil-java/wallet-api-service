--liquibase formatted sql

--changeset DanielK:1

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS wallets (
    wallet_id    uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    balance      decimal(10, 2) NOT NULL DEFAULT 0,
    updated      timestamp,
    created      timestamp NOT NULL DEFAULT current_timestamp
);

CREATE INDEX idx_wallets_updated ON wallets(updated);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    wallet_id        uuid NOT NULL REFERENCES wallets(wallet_id),
    operation_type   varchar(20) NOT NULL,
    amount           decimal(10, 2) NOT NULL,
    created          timestamp NOT NULL DEFAULT current_timestamp,
    status           varchar(20) NOT NULL,
    error_message    text
);

CREATE INDEX idx_transactions_wallet_id ON transactions(wallet_id);
