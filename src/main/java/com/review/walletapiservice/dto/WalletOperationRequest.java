package com.review.walletapiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Модель операции наз кошельком")
public class WalletOperationRequest {
    @Schema(description = "Идентификатор кошелька")
    private UUID walletId;
    @Schema(description = "Тип операции с кошельком")
    private OperationType operationType;
    @Schema(description = "Количество у.е.")
    private BigDecimal amount;
}
