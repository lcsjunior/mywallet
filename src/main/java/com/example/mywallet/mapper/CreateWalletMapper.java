package com.example.mywallet.mapper;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.model.Wallet;

public class CreateWalletMapper {

  private CreateWalletMapper() {
  }

  public static Wallet toModel(CreateWalletRequest createWalletRequest) {
    var wallet = new Wallet();
    wallet.setUserId(createWalletRequest.userId());
    wallet.setDisplayName(createWalletRequest.displayName());
    wallet.setType(createWalletRequest.type());
    return wallet;
  }
}
