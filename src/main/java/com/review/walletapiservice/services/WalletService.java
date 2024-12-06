package com.review.walletapiservice.services;


import com.review.walletapiservice.dto.WalletOperationRequest;
import com.review.walletapiservice.dto.WalletResponse;
import com.review.walletapiservice.entities.Wallet;
import com.review.walletapiservice.exceptions.ErrorResponseException;
import com.review.walletapiservice.exceptions.ErrorStatus;
import com.review.walletapiservice.operations.WalletOperation;
import com.review.walletapiservice.operations.WalletOperationHandler;
import com.review.walletapiservice.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletOperationHandler walletOperationHandler;
    private final TransactionService transactionService;

    public WalletResponse getBalance(UUID walletId) {
        Wallet wallet = findByUUID(walletId);

        return WalletResponse.convert(wallet);
    }

    @Transactional
    public WalletResponse handleOperation(WalletOperationRequest request) {
        Wallet wallet = findByUUID(request.getWalletId());

        WalletOperation walletOperation = walletOperationHandler.getOperation(request.getOperationType());

        String errorMessage = null;
        try {
            wallet.setBalance(walletOperation.apply(wallet.getBalance(), request.getAmount()));
        } catch (ErrorResponseException error) {
            errorMessage = error.getErrorStatus().getMessage();
            throw error;
        } finally {
            transactionService.save(wallet, request, errorMessage);
        }

        return WalletResponse.convert(walletRepository.save(wallet));
    }

    private Wallet findByUUID(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.WALLET_NOT_FOUND));
    }

    public WalletResponse createWallet() {
        return WalletResponse.convert(walletRepository.save(new Wallet().setBalance(BigDecimal.ZERO)));
    }
}
