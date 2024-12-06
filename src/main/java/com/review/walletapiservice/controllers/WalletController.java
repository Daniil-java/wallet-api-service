package com.review.walletapiservice.controllers;

import com.review.walletapiservice.dto.WalletOperationRequest;
import com.review.walletapiservice.dto.WalletResponse;
import com.review.walletapiservice.exceptions.ErrorResponse;
import com.review.walletapiservice.services.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Управление кошельком", description = "Методы работы с кошельком")
public class WalletController {
    private final WalletService walletService;

    @Operation(
            summary = "Проведение операций с кошельком",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = WalletResponse.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public WalletResponse handleOperation(
            @Parameter(description = "Модель запроса операции над кошельком", required = true)
            @RequestBody @Validated WalletOperationRequest request) {
        return walletService.handleOperation(request);
    }

    @Operation(
            summary = "Получение кошелька",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = WalletResponse.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{walletId}")
    public WalletResponse getBalance(
            @Parameter(description = "ID кошелька", required = true) @PathVariable UUID walletId) {
        return walletService.getBalance(walletId);
    }

    @Operation(
            summary = "Создание нового кошелька",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = WalletResponse.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public WalletResponse createWallet() {
        return walletService.createWallet();
    }
}
