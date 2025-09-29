package com.example.mywallet.mapper;

import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.model.Wallet;

public class GetWalletMapper {

  private GetWalletMapper() {
  }

  public static GetWalletResponse toResponse(Wallet wallet) {
    return new GetWalletResponse(
        wallet.getId(),
        wallet.getBalance(),
        wallet.getDisplayName(),
        wallet.getCreatedAt(),
        wallet.getUpdatedAt());
  }
}
