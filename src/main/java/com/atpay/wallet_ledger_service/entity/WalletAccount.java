package com.atpay.wallet_ledger_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletAccount {
    @Id
    @Column(name = "wallet_account_id", nullable = false, updatable = false, columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletAccountId;

    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(name = "balance", nullable = false, precision = 20, scale = 8)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}
