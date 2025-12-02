package com.atpay.wallet_ledger_service.service;

import org.springframework.stereotype.Service;

@Service
public interface IdempotencyService {

    public boolean checkIdempotency(String key);

    public boolean setIdempotency(String key, String value, int ttlSeconds);

    public String getValue(String key);
}
