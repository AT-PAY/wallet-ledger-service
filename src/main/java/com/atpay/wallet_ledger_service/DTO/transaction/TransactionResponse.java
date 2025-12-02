package com.atpay.wallet_ledger_service.DTO.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private UUID ledgerTransactionId;

    private UUID walletAccountId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private BigDecimal balanceAfter;

    private String referenceId;

    private OffsetDateTime createdAt;
}
