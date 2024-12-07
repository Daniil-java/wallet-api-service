package com.review.walletapiservice.dto;


import com.review.walletapiservice.entities.Wallet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Schema(description = "Модель кошелька")
public class WalletResponse {

    @Schema(description = "Идентификатор кошелька")
    private UUID walletId;
    @Schema(description = "Баланс кошелька")
    private BigDecimal balance;

    public static WalletResponse convert(Wallet wallet) {
        return new WalletResponse()
                .setBalance(wallet.getBalance())
                .setWalletId(wallet.getWalletId());
    }
}
