package com.example.mywallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(
    @NotNull(message = "{validation.amount.required}")
    @DecimalMin(value = "0.01", message = "{validation.amount.min}")
    @Digits(integer = 20, fraction = 2, message = "{validation.amount.decimal.places}")
    BigDecimal amount
) {
}
