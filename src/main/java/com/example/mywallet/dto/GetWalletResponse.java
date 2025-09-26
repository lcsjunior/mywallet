package com.example.mywallet.dto;

import com.example.mywallet.model.WalletType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record GetWalletResponse(
    UUID walletId,
    BigDecimal balance,
    String displayName,
    WalletType type,
    Instant createdAt,
    Instant updatedAt
) {
}
