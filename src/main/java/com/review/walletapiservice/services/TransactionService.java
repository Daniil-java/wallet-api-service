package com.review.walletapiservice.services;

import com.review.walletapiservice.dto.TransactionStatus;
import com.review.walletapiservice.dto.WalletOperationRequest;
import com.review.walletapiservice.entities.Transaction;
import com.review.walletapiservice.entities.Wallet;
import com.review.walletapiservice.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public void save(Wallet wallet, WalletOperationRequest walletOperationRequest, String errorMessage) {
        transactionRepository.save(
                new Transaction()
                        .setWallet(wallet)
                        .setOperationType(walletOperationRequest.getOperationType())
                        .setAmount(walletOperationRequest.getAmount())
                        .setStatus(errorMessage == null ? TransactionStatus.SUCCESS : TransactionStatus.DENIED)
                        .setErrorMessage(errorMessage)
        );
    }
}
