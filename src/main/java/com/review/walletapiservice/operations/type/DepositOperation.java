package com.review.walletapiservice.operations.type;

import com.review.walletapiservice.dto.OperationType;
import com.review.walletapiservice.operations.WalletOperationProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositOperation implements WalletOperationProcessor {
    @Override
    public BigDecimal apply(BigDecimal balance, BigDecimal amount) {
        return balance.add(amount);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.DEPOSIT;
    }
}
