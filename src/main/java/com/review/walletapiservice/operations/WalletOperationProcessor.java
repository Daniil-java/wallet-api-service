package com.review.walletapiservice.operations;

import com.review.walletapiservice.dto.OperationType;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public interface WalletOperationProcessor {
    BigDecimal apply(BigDecimal balance, BigDecimal amount);
    OperationType getOperationType();

    @Autowired
    default void registerMyself(WalletOperationProcessorHandler walletOperationProcessorHandler) {
        walletOperationProcessorHandler.register(getOperationType(), this);
    }
}
