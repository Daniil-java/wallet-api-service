package com.review.walletapiservice.entities;

import com.review.walletapiservice.dto.OperationType;
import com.review.walletapiservice.dto.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@Accessors(chain = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private BigDecimal amount;

    @CreationTimestamp
    private OffsetDateTime created;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String errorMessage;
}
