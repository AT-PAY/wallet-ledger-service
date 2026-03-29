package com.atpay.wallet_ledger_service.service.impl;

import com.atpay.wallet_ledger_service.service.IdempotencyService;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    @Override
    public boolean checkIdempotency(String key) {
        return false;
    }

    @Override
    public boolean setIdempotency(String key, String value, int ttlSeconds) {
        return false;
    }

    @Override
    public String getValue(String key) {
        return "";
    }
}
