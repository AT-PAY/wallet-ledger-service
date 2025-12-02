-- V1 - Initial schema for wallet-ledger-service

-- =========================
-- wallet_account
-- =========================
CREATE TABLE wallet_account
(
    wallet_account_id UUID PRIMARY KEY,
    user_id           UUID           NOT NULL,
    balance           NUMERIC(20, 8) NOT NULL DEFAULT 0,
    currency          VARCHAR(8)     NOT NULL,
    status            VARCHAR(16)    NOT NULL DEFAULT 'active',
    created_at        TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ    NOT NULL DEFAULT now(),
    deleted_at        TIMESTAMPTZ,
    is_deleted        BOOLEAN        NOT NULL DEFAULT FALSE
);

-- =========================
-- ledger_transaction
-- =========================
CREATE TABLE ledger_transaction
(
    ledger_transaction_id UUID PRIMARY KEY,
    wallet_account_id     UUID           NOT NULL,
    type                  VARCHAR(32)    NOT NULL,
    amount                NUMERIC(20, 8) NOT NULL,
    currency              VARCHAR(8)     NOT NULL,
    status                VARCHAR(16)    NOT NULL DEFAULT 'pending',
    reference_id          VARCHAR(128),
    metadata              JSONB,
    created_at            TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ    NOT NULL DEFAULT now(),
    deleted_at            TIMESTAMPTZ,
    is_deleted            BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_ledger_transaction_wallet
        FOREIGN KEY (wallet_account_id)
            REFERENCES wallet_account (wallet_account_id)
);

-- =========================
-- transaction_journal
-- =========================
CREATE TABLE transaction_journal
(
    transaction_journal_id UUID PRIMARY KEY,
    ledger_transaction_id  UUID        NOT NULL,
    action                 VARCHAR(32) NOT NULL,
    performed_by           VARCHAR(64),
    timestamp              TIMESTAMPTZ NOT NULL DEFAULT now(),
    metadata               JSONB,
    CONSTRAINT fk_transaction_journal_ledger
        FOREIGN KEY (ledger_transaction_id)
            REFERENCES ledger_transaction (ledger_transaction_id)
);

-- =========================
-- wallet_event_log
-- =========================
CREATE TABLE wallet_event_log
(
    wallet_event_log_id UUID PRIMARY KEY,
    wallet_account_id   UUID        NOT NULL,
    event_type          VARCHAR(64) NOT NULL,
    description         TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_wallet_event_log_wallet
        FOREIGN KEY (wallet_account_id)
            REFERENCES wallet_account (wallet_account_id)
);
