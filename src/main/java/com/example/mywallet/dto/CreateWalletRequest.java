package com.example.mywallet.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequest(
    @NotNull(message = "User Id is required")
    UUID userId
) {
}
