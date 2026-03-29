package com.atpay.wallet_ledger_service.service;

public interface IdempotencyService {

    public boolean checkIdempotency(String key);

    public boolean setIdempotency(String key, String value, int ttlSeconds);

    public String getValue(String key);
}
