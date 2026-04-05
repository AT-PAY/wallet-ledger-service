package com.atpay.wallet_ledger_service.controller;

import com.atpay.wallet_ledger_service.DTO.ApiResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.DepositRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.JournalResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.PaymentRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.RefundRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionTransferResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransferRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.WithdrawRequest;
import com.atpay.wallet_ledger_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Description("WL_A101")
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionResponse> deposit(@RequestBody DepositRequest request) {
        return ApiResponse.success(transactionService.deposit(request));
    }

    @Description("WL_A102")
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionResponse> withdraw(@RequestBody WithdrawRequest request) {
        return ApiResponse.success(transactionService.withdraw(request));
    }

    @Description("WL_A103")
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionTransferResponse> transfer(@RequestBody TransferRequest request) {
        return ApiResponse.success(transactionService.transfer(request));
    }

    @Description("WL_A104")
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionResponse> payment(@RequestBody PaymentRequest request) {
        return ApiResponse.success(transactionService.payment(request));
    }

    @Description("WL_A105")
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionResponse> refund(@RequestBody RefundRequest request) {
        return ApiResponse.success(transactionService.refund(request));
    }

    @Description("WL_A106")
    @GetMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionResponse> getTransaction(@PathVariable String transactionId) {
        return ApiResponse.success(transactionService.getTransaction(UUID.fromString(transactionId)));
    }

    @Description("WL_A301")
    @GetMapping("/{transactionId}/journal")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<JournalResponse>> getJournal(@PathVariable String transactionId) {
        return ApiResponse.success(transactionService.getJournal(UUID.fromString(transactionId)));
    }
}
