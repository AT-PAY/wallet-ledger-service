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
public class BalanceResponse {

    private UUID walletAccountId;

    private BigDecimal balance;

    private String currency;

    private OffsetDateTime updatedAt;
}
