package com.atpay.wallet_ledger_service.repository;

import com.atpay.wallet_ledger_service.entity.TransactionJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionJournalRepository extends JpaRepository<TransactionJournal, UUID> {
}
