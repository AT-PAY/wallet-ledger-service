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
public class TransactionTransferResponse {

    private UUID ledgerTransactionFromId;

    private UUID walletAccountFromId;

    private UUID ledgerTransactionToId;

    private UUID walletAccountToId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private BigDecimal balanceFromAfter;

    private BigDecimal balanceToAfter;

    private String referenceId;

    private OffsetDateTime transactionFromCreatedAt;

    private OffsetDateTime transactionToCreatedAt;
}
