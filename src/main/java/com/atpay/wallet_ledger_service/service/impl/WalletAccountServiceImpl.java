package com.atpay.wallet_ledger_service.service.impl;

import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountRequest;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountResponse;
import com.atpay.wallet_ledger_service.converter.WalletAccountConverter;
import com.atpay.wallet_ledger_service.repository.WalletAccountRepository;
import com.atpay.wallet_ledger_service.service.WalletAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletAccountServiceImpl implements WalletAccountService {

    private final WalletAccountRepository walletAccountRepository;

    @Override
    public void createWalletAccount(WalletAccountRequest request) {
        walletAccountRepository.save(WalletAccountConverter.INSTANCE.toEntity(request));
    }

    @Override
    public WalletAccountResponse getWalletAccount(UUID walletAccountId) {
        return WalletAccountConverter.INSTANCE.toResponse(walletAccountRepository.findById(walletAccountId).orElse(null));
    }

    @Override
    public void freezeWalletAccount(UUID walletAccountId) {
        walletAccountRepository.findById(walletAccountId).ifPresent(walletAccount -> walletAccount.setStatus("freeze"));
    }

    @Override
    public void unfreezeWalletAccount(UUID walletAccountId) {
        walletAccountRepository.findById(walletAccountId).ifPresent(walletAccount -> walletAccount.setStatus("active"));
    }
}
