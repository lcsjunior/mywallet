package com.example.mywallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateWalletRequest(
    @NotNull(message = "{validation.user_id.required}")
    UUID userId,

    @Schema(example = "null")
    @Size(min = 3, max = 50, message = "{validation.display_name.size}")
    String displayName
) {
}
