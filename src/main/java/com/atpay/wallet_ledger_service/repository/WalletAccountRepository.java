package com.atpay.wallet_ledger_service.repository;

import com.atpay.wallet_ledger_service.entity.WalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletAccountRepository extends JpaRepository<WalletAccount, UUID> {
}
