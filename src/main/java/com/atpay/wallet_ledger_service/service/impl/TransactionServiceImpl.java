package com.atpay.wallet_ledger_service.service.impl;

import com.atpay.wallet_ledger_service.DTO.transaction.BalanceChangeRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.DepositRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.HistoryTransactionWalletResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.JournalResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.PaymentRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.RefundRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionTransferResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransferRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.WithdrawRequest;
import com.atpay.wallet_ledger_service.converter.LedgerTransactionConverter;
import com.atpay.wallet_ledger_service.entity.LedgerTransaction;
import com.atpay.wallet_ledger_service.entity.TransactionJournal;
import com.atpay.wallet_ledger_service.entity.WalletAccount;
import com.atpay.wallet_ledger_service.event.TransactionCompletedEvent;
import com.atpay.wallet_ledger_service.repository.LedgerTransactionRepository;
import com.atpay.wallet_ledger_service.repository.TransactionJournalRepository;
import com.atpay.wallet_ledger_service.repository.WalletAccountRepository;
import com.atpay.wallet_ledger_service.service.IdempotencyService;
import com.atpay.wallet_ledger_service.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final IdempotencyService idempotencyService;

    private final LedgerTransactionRepository transactionRepository;

    private final WalletAccountRepository walletAccountRepository;

    private final TransactionJournalRepository transactionJournalRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final ObjectMapper objectMapper;

    private final LedgerTransactionConverter ledgerTransactionConverter;

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        return processTransaction(
                request,
                "deposit",
                "credit",
                BigDecimal::add
        );
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        return processTransaction(
                request,
                "withdraw",
                "debit",
                (currentBalance, amount) -> {
                    BigDecimal after = currentBalance.subtract(amount);
                    if (after.compareTo(BigDecimal.ZERO) < 0) {
                        // tuỳ bạn: throw BusinessException custom
                        throw new RuntimeException("INSUFFICIENT_BALANCE");
                    }
                    return after;
                }
        );
    }

    @Override
    @Transactional
    public TransactionTransferResponse transfer(TransferRequest request) {
        String idemKey = "idempotency:" + request.getIdempotencyKey();

        // 1. Check idempotency
        if (idempotencyService.checkIdempotency(idemKey)) {
            String value = idempotencyService.getValue(idemKey);
            return objectMapper.convertValue(value, TransactionTransferResponse.class);
        }

        // 2. Load wallet
        WalletAccount walletAccountFrom = walletAccountRepository
                .findById(request.getWalletAccountIdFrom())
                .orElseThrow(() -> new RuntimeException("WALLET_NOT_FOUND"));

        WalletAccount walletAccountTo = walletAccountRepository
                .findById(request.getWalletAccountIdTo())
                .orElseThrow(() -> new RuntimeException("WALLET_NOT_FOUND"));

        // 3. Create ledger transaction (pending)
        LedgerTransaction transactionFrom = transactionRepository.saveAndFlush(
                LedgerTransaction.builder()
                        .walletAccount(walletAccountFrom)
                        .type("transfer")
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .status("pending")
                        .referenceId(request.getReferenceId())
                        .metadata(request.getMetadata())
                        .build());

        LedgerTransaction transactionTo = transactionRepository.saveAndFlush(
                LedgerTransaction.builder()
                        .walletAccount(walletAccountTo)
                        .type("transfer")
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .status("pending")
                        .referenceId(request.getReferenceId())
                        .metadata(request.getMetadata())
                        .build());

        // 4. Journal init
        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transactionFrom)
                        .action("init")
                        .performedBy("system")
                        .metadata(Map.of(
                                "idempotencyKey", request.getIdempotencyKey(),
                                "request", request
                        ))
                        .build()
        );

        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transactionTo)
                        .action("init")
                        .performedBy("system")
                        .metadata(Map.of(
                                "idempotencyKey", request.getIdempotencyKey(),
                                "request", request
                        ))
                        .build()
        );

        // 5. Update wallet amount (using strategy)
        BigDecimal currentBalanceFrom = walletAccountFrom.getBalance();
        BigDecimal afterBalanceFrom = currentBalanceFrom.subtract(request.getAmount());

        if (afterBalanceFrom.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("INSUFFICIENT_BALANCE");
        }

        BigDecimal currentBalanceTo = walletAccountTo.getBalance();
        BigDecimal afterBalanceTo = currentBalanceTo.add(request.getAmount());

        walletAccountFrom.setBalance(afterBalanceFrom);
        walletAccountFrom = walletAccountRepository.saveAndFlush(walletAccountFrom);

        walletAccountTo.setBalance(afterBalanceTo);
        walletAccountTo = walletAccountRepository.saveAndFlush(walletAccountTo);

        // 6. Journal credit/debit
        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transactionFrom)
                        .action("debit")
                        .performedBy("system")
                        .metadata(Map.of(
                                "beforeBalanceFrom", currentBalanceFrom,
                                "afterBalance", walletAccountFrom.getBalance()
                        ))
                        .build()
        );

        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transactionTo)
                        .action("credit")
                        .performedBy("system")
                        .metadata(Map.of(
                                "beforeBalanceFrom", currentBalanceTo,
                                "afterBalance", walletAccountTo.getBalance()
                        ))
                        .build()
        );

        // 7. Update transaction status -> success
        String currentTransactionFromStatus = transactionFrom.getStatus();
        transactionFrom.setStatus("success");
        transactionFrom = transactionRepository.saveAndFlush(transactionFrom);

        String currentTransactionToStatus = transactionTo.getStatus();
        transactionTo.setStatus("success");
        transactionTo = transactionRepository.saveAndFlush(transactionTo);

        // 8. Journal confirm
        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transactionFrom)
                        .action("confirm")
                        .performedBy("system")
                        .metadata(Map.of(
                                "beforeTransactionFromStatus", currentTransactionFromStatus,
                                "afterTransactionFromStatus", transactionFrom.getStatus()
                        ))
                        .build()
        );

        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transactionTo)
                        .action("confirm")
                        .performedBy("system")
                        .metadata(Map.of(
                                "beforeTransactionToStatus", currentTransactionToStatus,
                                "afterTransactionToStatus", transactionTo.getStatus()
                        ))
                        .build()
        );

        // 9. Build response
        TransactionTransferResponse response = TransactionTransferResponse.builder()
                .ledgerTransactionFromId(transactionFrom.getLedgerTransactionId())
                .ledgerTransactionToId(transactionTo.getLedgerTransactionId())
                .walletAccountFromId(walletAccountFrom.getWalletAccountId())
                .walletAccountToId(walletAccountTo.getWalletAccountId())
                .amount(transactionFrom.getAmount())
                .currency(request.getCurrency())
                .status(transactionFrom.getStatus())
                .balanceFromAfter(walletAccountFrom.getBalance())
                .balanceToAfter(walletAccountTo.getBalance())
                .referenceId(transactionFrom.getReferenceId())
                .transactionFromCreatedAt(transactionFrom.getCreatedAt())
                .transactionToCreatedAt(transactionTo.getCreatedAt())
                .build();

        // 10. Save idempotent result
        eventPublisher.publishEvent(
                new TransactionCompletedEvent(response, request.getIdempotencyKey())
        );

        return response;
    }

    @Override
    public TransactionResponse payment(PaymentRequest request) {
        return processTransaction(
                request,
                "payment",
                "debit",
                (currentBalance, amount) -> {
                    BigDecimal after = currentBalance.subtract(amount);
                    if (after.compareTo(BigDecimal.ZERO) < 0) {
                        throw new RuntimeException("INSUFFICIENT_BALANCE");
                    }
                    return after;
                }
        );
    }

    @Override
    public TransactionResponse refund(RefundRequest request) {
        Map<String, Object> metadata = new HashMap<>();
        if (request.getMetadata() != null) {
            metadata.putAll(request.getMetadata());
        }

        if (request.getReferenceId() != null) {
            metadata.put("originalReferenceId", request.getReferenceId());
        }
        request.setMetadata(metadata);

        return processTransaction(
                request,
                "refund",    // transactionType
                "credit",    // journal action
                BigDecimal::add
        );

    }

    @Override
    public TransactionResponse getTransaction(UUID transactionId) {
        LedgerTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("TRANSACTION_NOT_FOUND"));
        return ledgerTransactionConverter.toResponse(transaction);
    }

    @Override
    public HistoryTransactionWalletResponse getHistoryTransactionWallet(UUID walletId) {
        List<LedgerTransaction> transactions = transactionRepository.findByWalletAccount_WalletAccountId(walletId);
        List<TransactionResponse> responses = ledgerTransactionConverter.toResponseList(transactions);
        return HistoryTransactionWalletResponse.builder()
                .transactions(responses)
                .total(responses.size())
                .build();
    }

    @Override
    public List<JournalResponse> getJournal(UUID transactionId) {
        return transactionJournalRepository.findByLedgerTransaction_LedgerTransactionId(transactionId)
                .stream()
                .map(j -> JournalResponse.builder()
                        .transactionJournalId(j.getTransactionJournalId())
                        .ledgerTransactionId(transactionId)
                        .action(j.getAction())
                        .performedBy(j.getPerformedBy())
                        .timestamp(j.getTimestamp())
                        .build())
                .toList();
    }

    private TransactionResponse processTransaction(
            BalanceChangeRequest request,
            String transactionType,           // "deposit" | "withdraw"
            String balanceAction,             // "credit"  | "debit"
            BiFunction<BigDecimal, BigDecimal, BigDecimal> balanceCalculator
    ) {
        String idemKey = "idempotency:" + request.getIdempotencyKey();

        // 1. Check idempotency
        if (idempotencyService.checkIdempotency(idemKey)) {
            String value = idempotencyService.getValue(idemKey);
            return objectMapper.convertValue(value, TransactionResponse.class);
        }

        // 2. Load wallet
        WalletAccount walletAccount = walletAccountRepository
                .findById(request.getWalletAccountId())
                .orElseThrow(() -> new RuntimeException("WALLET_NOT_FOUND"));

        // 3. Create ledger transaction (pending)
        LedgerTransaction transaction = transactionRepository.saveAndFlush(
                LedgerTransaction.builder()
                        .walletAccount(walletAccount)
                        .type(transactionType)
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .status("pending")
                        .referenceId(request.getReferenceId())
                        .metadata(request.getMetadata())
                        .build()
        );

        // 4. Journal init
        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transaction)
                        .action("init")
                        .performedBy("system")
                        .metadata(Map.of(
                                "idempotencyKey", request.getIdempotencyKey(),
                                "request", request
                        ))
                        .build()
        );

        // 5. Update wallet amount (using strategy)
        BigDecimal currentBalance = walletAccount.getBalance();
        BigDecimal afterBalance = balanceCalculator.apply(currentBalance, request.getAmount());

        walletAccount.setBalance(afterBalance);
        walletAccount = walletAccountRepository.saveAndFlush(walletAccount);

        // 6. Journal credit/debit
        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transaction)
                        .action(balanceAction) // "credit" or "debit"
                        .performedBy("system")
                        .metadata(Map.of(
                                "beforeBalance", currentBalance,
                                "afterBalance", walletAccount.getBalance()
                        ))
                        .build()
        );

        // 7. Update transaction status -> success
        String currentTransactionStatus = transaction.getStatus();
        transaction.setStatus("success");
        transaction = transactionRepository.saveAndFlush(transaction);

        // 8. Journal confirm
        transactionJournalRepository.save(
                TransactionJournal.builder()
                        .ledgerTransaction(transaction)
                        .action("confirm")
                        .performedBy("system")
                        .metadata(Map.of(
                                "beforeTransactionStatus", currentTransactionStatus,
                                "afterTransactionStatus", transaction.getStatus()
                        ))
                        .build()
        );

        // 9. Build response
        TransactionResponse response = TransactionResponse.builder()
                .ledgerTransactionId(transaction.getLedgerTransactionId())
                .walletAccountId(walletAccount.getWalletAccountId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .balanceAfter(walletAccount.getBalance())
                .referenceId(transaction.getReferenceId())
                .createdAt(transaction.getCreatedAt())
                .build();

        // 10. Save idempotent result
        eventPublisher.publishEvent(
                new TransactionCompletedEvent(response, request.getIdempotencyKey())
        );

        return response;
    }
}
