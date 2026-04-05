package com.atpay.wallet_ledger_service.DTO.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest implements BalanceChangeRequest {

    private UUID walletAccountId;

    private BigDecimal amount;

    private String currency;

    private String source;

    private String referenceId;

    private UUID idempotencyKey;

    private Map<String, Object> metadata;
}
