package com.atpay.wallet_ledger_service.service.impl;

import com.atpay.wallet_ledger_service.DTO.wallet.BalanceResponse;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountRequest;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountResponse;
import com.atpay.wallet_ledger_service.converter.WalletAccountConverter;
import com.atpay.wallet_ledger_service.entity.WalletAccount;
import com.atpay.wallet_ledger_service.repository.WalletAccountRepository;
import com.atpay.wallet_ledger_service.service.WalletAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletAccountServiceImpl implements WalletAccountService {

    private final WalletAccountRepository walletAccountRepository;
    private final WalletAccountConverter walletAccountConverter;

    @Override
    public void createWalletAccount(WalletAccountRequest request) {
        walletAccountRepository.save(walletAccountConverter.toEntity(request));
    }

    @Override
    public WalletAccountResponse getWalletAccount(UUID walletAccountId) {
        return walletAccountConverter.toResponse(walletAccountRepository.findById(walletAccountId).orElse(null));
    }

    @Override
    public void freezeWalletAccount(UUID walletAccountId) {
        walletAccountRepository.findById(walletAccountId).ifPresent(walletAccount -> {
            walletAccount.setStatus("freeze");
            walletAccountRepository.save(walletAccount);
        });
    }

    @Override
    public void unfreezeWalletAccount(UUID walletAccountId) {
        walletAccountRepository.findById(walletAccountId).ifPresent(walletAccount -> {
            walletAccount.setStatus("active");
            walletAccountRepository.save(walletAccount);
        });
    }

    @Override
    public BalanceResponse getBalance(UUID walletAccountId) {
        WalletAccount walletAccount = walletAccountRepository.findById(walletAccountId)
                .orElseThrow(() -> new RuntimeException("WALLET_NOT_FOUND"));
        return BalanceResponse.builder()
                .walletAccountId(walletAccount.getWalletAccountId())
                .balance(walletAccount.getBalance())
                .currency(walletAccount.getCurrency())
                .updatedAt(walletAccount.getUpdatedAt())
                .build();
    }
}
