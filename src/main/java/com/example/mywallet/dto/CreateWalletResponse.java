package com.example.mywallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateWalletResponse(
    UUID walletId,
    BigDecimal balance
) {
}
