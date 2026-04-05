package com.atpay.wallet_ledger_service.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class TransactionCompletedEvent {

    private final Object response;

    private final UUID idempotencyKey;
}
