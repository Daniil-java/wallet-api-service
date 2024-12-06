package com.review.walletapiservice.operations.type;

import com.review.walletapiservice.dto.OperationType;
import com.review.walletapiservice.exceptions.ErrorResponseException;
import com.review.walletapiservice.exceptions.ErrorStatus;
import com.review.walletapiservice.operations.WalletOperation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawOperation implements WalletOperation {
    @Override
    public BigDecimal apply(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            return balance.subtract(amount);
        } else {
            throw new ErrorResponseException(ErrorStatus.WALLET_NOT_ENOUGH_BALANCE);
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.DEPOSIT;
    }
}
