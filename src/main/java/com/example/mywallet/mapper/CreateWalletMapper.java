package com.example.mywallet.mapper;

import com.example.mywallet.dto.CreateWalletResponse;
import com.example.mywallet.model.Wallet;

public class CreateWalletMapper {

  private CreateWalletMapper() {
  }

  public static CreateWalletResponse toResponse(Wallet wallet) {
    return new CreateWalletResponse(wallet.getWalletId(), wallet.getBalance());
  }
}
