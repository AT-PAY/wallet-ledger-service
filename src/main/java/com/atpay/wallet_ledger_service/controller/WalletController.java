package com.atpay.wallet_ledger_service.controller;

import com.atpay.wallet_ledger_service.DTO.ApiResponse;
import com.atpay.wallet_ledger_service.DTO.transaction.HistoryTransactionWalletResponse;
import com.atpay.wallet_ledger_service.DTO.wallet.BalanceResponse;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountRequest;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountResponse;
import com.atpay.wallet_ledger_service.service.TransactionService;
import com.atpay.wallet_ledger_service.service.WalletAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletAccountService walletAccountService;
    private final TransactionService transactionService;

    @Description("WL_A001")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Boolean> createWallet(@RequestBody WalletAccountRequest request) {
        walletAccountService.createWalletAccount(request);
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A002")
    @GetMapping("/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WalletAccountResponse> getWallet(@PathVariable String walletId) {
        return ApiResponse.success(walletAccountService.getWalletAccount(UUID.fromString(walletId)));
    }

    @Description("WL_A003")
    @PatchMapping("/{walletId}/freeze")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> freezeWallet(@PathVariable String walletId) {
        walletAccountService.freezeWalletAccount(UUID.fromString(walletId));
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A004")
    @PatchMapping("/{walletId}/unfreeze")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> unfreezeWallet(@PathVariable String walletId) {
        walletAccountService.unfreezeWalletAccount(UUID.fromString(walletId));
        return ApiResponse.success(Boolean.TRUE);
    }

    @Description("WL_A201")
    @GetMapping("/{walletId}/balance")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BalanceResponse> getBalance(@PathVariable String walletId) {
        return ApiResponse.success(walletAccountService.getBalance(UUID.fromString(walletId)));
    }

    @Description("WL_A107")
    @GetMapping("/{walletId}/transaction")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<HistoryTransactionWalletResponse> getHistoryTransactionWallet(@PathVariable String walletId) {
        return ApiResponse.success(transactionService.getHistoryTransactionWallet(UUID.fromString(walletId)));
    }
}
