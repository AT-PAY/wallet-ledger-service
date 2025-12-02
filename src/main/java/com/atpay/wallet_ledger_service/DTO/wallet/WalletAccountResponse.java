package com.atpay.wallet_ledger_service.DTO.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletAccountResponse {

    private UUID walletAccountId;

    private UUID userId;

    private BigDecimal balance;

    private String currency;

    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
