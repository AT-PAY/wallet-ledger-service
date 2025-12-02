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

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_event_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEventLog {
    @Id
    @Column(name = "wallet_event_log_id", nullable = false, updatable = false, columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletEventLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_account_id", nullable = false, columnDefinition = "uuid")
    private WalletAccount walletAccount;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
