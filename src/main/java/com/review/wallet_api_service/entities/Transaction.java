package com.review.wallet_api_service.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@Accessors(chain = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID transactionId;

    @ManyToOne
    @JoinColumn(name = "walletId", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private String operationType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Column(nullable = false)
    private String status;

    private String errorMessage;
}
