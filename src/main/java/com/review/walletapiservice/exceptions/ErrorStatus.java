package com.review.walletapiservice.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    WALLET_NOT_FOUND(HttpStatus.BAD_REQUEST, "Wallet not found!"),
    WALLET_NOT_ENOUGH_BALANCE(HttpStatus.BAD_REQUEST, "Not enough balance for withdrawal"),
    WALLET_INCORRECT_OPERATION(HttpStatus.BAD_REQUEST, "Unknown operation type");

    private HttpStatus httpStatus;
    private String message;
}
