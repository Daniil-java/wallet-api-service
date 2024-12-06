package com.review.walletapiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {
    DEPOSIT,
    WITHDRAW;
}
