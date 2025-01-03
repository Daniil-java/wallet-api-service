package com.review.walletapiservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Data
@Accessors(chain = true)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID walletId;

    private BigDecimal balance;

    @UpdateTimestamp
    private OffsetDateTime updated;

    @CreationTimestamp
    private OffsetDateTime created;
}
