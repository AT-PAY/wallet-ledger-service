package com.atpay.wallet_ledger_service.event;

import com.atpay.wallet_ledger_service.service.IdempotencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TransactionListener {

    private final IdempotencyService idempotencyService;

    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @SneakyThrows
    public void handleDepositCompleted(TransactionCompletedEvent event) {
        idempotencyService.setIdempotency("idempotency:" + event.getIdempotencyKey(),
                objectMapper.writeValueAsString(event.getResponse()), 900);
    }
}
