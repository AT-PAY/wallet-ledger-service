package com.atpay.wallet_ledger_service.service;

import com.atpay.wallet_ledger_service.DTO.wallet.BalanceResponse;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountRequest;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface WalletAccountService {

    void createWalletAccount(WalletAccountRequest request);

    WalletAccountResponse getWalletAccount(UUID walletAccountId);

    void freezeWalletAccount(UUID walletAccountId);

    void unfreezeWalletAccount(UUID walletAccountId);

    BalanceResponse getBalance(UUID walletAccountId);

}
