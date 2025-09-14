package com.example.mywallet.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequest(
    @NotNull UUID userId
) {
}
