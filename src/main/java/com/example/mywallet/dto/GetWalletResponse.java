package com.example.mywallet.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record GetWalletResponse(
    UUID walletId,
    BigDecimal balance,
    String displayName,
    Instant createdAt,
    Instant updatedAt
) {
}
