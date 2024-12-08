package com.review.walletapiservice.operations;

import com.review.walletapiservice.dto.OperationType;
import com.review.walletapiservice.exceptions.ErrorResponseException;
import com.review.walletapiservice.exceptions.ErrorStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WalletOperationProcessorHandler {
    private final Map<OperationType, WalletOperationProcessor> operationMap = new ConcurrentHashMap<>();

    public void register(OperationType operationType, WalletOperationProcessor walletOperationProcessor) {
        operationMap.put(operationType, walletOperationProcessor);
    }

    public WalletOperationProcessor getOperation(OperationType type) {
        WalletOperationProcessor operation = operationMap.get(type);
        if (operation == null) {
            throw new ErrorResponseException(ErrorStatus.WALLET_INCORRECT_OPERATION);
        }
        return operation;
    }
}
