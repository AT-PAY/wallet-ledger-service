package com.atpay.wallet_ledger_service.event;

import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class TransactionCompletedEvent {

    private final Object response;

    private final UUID idempotencyKey;
}
