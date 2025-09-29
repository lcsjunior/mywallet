package com.example.mywallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequest(
    @NotNull(message = "User Id is required")
    UUID userId,

    @Schema(example = "null")
    String displayName
) {
}
