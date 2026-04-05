package com.atpay.wallet_ledger_service.DTO.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalResponse {

    private UUID transactionJournalId;

    private UUID ledgerTransactionId;

    private String action;

    private String performedBy;

    private OffsetDateTime timestamp;
}
