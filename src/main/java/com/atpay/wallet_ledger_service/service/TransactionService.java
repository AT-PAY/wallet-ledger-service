package com.atpay.wallet_ledger_service.service;

import com.atpay.wallet_ledger_service.DTO.transaction.DepositRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.HistoryTransactionWalletResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.PaymentRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.RefundRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionTransferResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransferRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.WithdrawRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TransactionService {

    TransactionResponse deposit(DepositRequest request);

    TransactionResponse withdraw(WithdrawRequest request);

    TransactionTransferResponse transfer(TransferRequest request);

    TransactionResponse payment(PaymentRequest request);

    TransactionResponse refund(RefundRequest request);

    TransactionResponse getTransaction(UUID transactionId);

    HistoryTransactionWalletResponse getHistoryTransactionWallet(UUID walletId);
}
