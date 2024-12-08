package com.review.walletapiservice.services;


import com.review.walletapiservice.dto.WalletOperationRequest;
import com.review.walletapiservice.dto.WalletResponse;
import com.review.walletapiservice.entities.Wallet;
import com.review.walletapiservice.exceptions.ErrorResponseException;
import com.review.walletapiservice.exceptions.ErrorStatus;
import com.review.walletapiservice.operations.WalletOperationProcessor;
import com.review.walletapiservice.operations.WalletOperationProcessorHandler;
import com.review.walletapiservice.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletOperationProcessorHandler walletOperationProcessorHandler;
    private final TransactionService transactionService;
    private final RedissonClient redissonClient;

    public WalletResponse getBalance(UUID walletId) {
        Wallet wallet = findByUUID(walletId);

        return WalletResponse.convert(wallet);
    }

    @Transactional
    public WalletResponse handleOperation(WalletOperationRequest request) {
        if (!walletRepository.existsById(request.getWalletId())) {
            throw new ErrorResponseException(ErrorStatus.WALLET_NOT_FOUND);
        }

        RMap<UUID, WalletOperationRequest> map = redissonClient.getMap("walletOperations");
        RLock lock = map.getFairLock(request.getWalletId());

        Wallet wallet = null;
        String errorMessage = null;
        try {
            lock.lock();

            wallet = findByUUID(request.getWalletId());
            WalletOperationProcessor walletOperationProcessor = walletOperationProcessorHandler.getOperation(request.getOperationType());

            wallet.setBalance(walletOperationProcessor.apply(wallet.getBalance(), request.getAmount()));

            return WalletResponse.convert(walletRepository.save(wallet));
        } catch (ErrorResponseException error) {
            errorMessage = error.getErrorStatus().getMessage();
            throw error;
        } catch (WriteRedisConnectionException error) {
            throw new ErrorResponseException(ErrorStatus.REDIS_ERROR);
        } finally {
            if (wallet != null) {
                //Ситуация, когда wallet == null может возникнуть только в случае отстуствия соединения с Redis
                transactionService.save(wallet, request, errorMessage);
            }
            lock.unlock();
        }
    }

    private Wallet findByUUID(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.WALLET_NOT_FOUND));
    }

    public WalletResponse createWallet() {
        return WalletResponse.convert(walletRepository.save(new Wallet().setBalance(BigDecimal.ZERO)));
    }
}
