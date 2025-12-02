package com.atpay.wallet_ledger_service.controller;

import com.atpay.wallet_ledger_service.DTO.ApiResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.DepositRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.PaymentRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.RefundRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.TransferRequest;
import com.atpay.wallet_ledger_service.DTO.transaction.WithdrawRequest;
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

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    // TODO: Transaction service

    @Description("WL_A101")
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> deposit(@RequestBody DepositRequest request) {
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A102")
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> withdraw(@RequestBody WithdrawRequest request) {
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A103")
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> transfer(@RequestBody TransferRequest request) {
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A104")
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> payment(@RequestBody PaymentRequest request) {
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A105")
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> refund(@RequestBody RefundRequest request) {
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A106")
    @GetMapping("/transaction/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TransactionResponse> getTransaction(@PathVariable String transactionId) {
        return ApiResponse.success(null);
    }
}
