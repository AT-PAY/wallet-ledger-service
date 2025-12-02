package com.atpay.wallet_ledger_service.DTO.transaction;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface BalanceChangeRequest {
    UUID getIdempotencyKey();
    UUID getWalletAccountId();
    BigDecimal getAmount();
    String getCurrency();
    String getReferenceId();
    Map<String, Object> getMetadata();
}
