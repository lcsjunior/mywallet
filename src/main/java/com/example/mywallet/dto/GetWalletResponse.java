package com.example.mywallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record GetWalletResponse(
    UUID walletId,
    BigDecimal balance
) {
}
