package com.atpay.wallet_ledger_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "transaction_journal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionJournal {
    @Id
    @Column(name = "transaction_journal_id", nullable = false, updatable = false, columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionJournalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_transaction_id", nullable = false, columnDefinition = "uuid")
    private LedgerTransaction ledgerTransaction;

    /*
        init
        credit
        debit
        confirm
        rollback
     */
    @Column(name = "action", nullable = false, length = 32)
    private String action;

    @Column(name = "performed_by", length = 64)
    private String performedBy;

    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private OffsetDateTime timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;
}
