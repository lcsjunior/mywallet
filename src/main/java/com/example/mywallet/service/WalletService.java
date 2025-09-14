package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.CreateWalletResponse;
import com.example.mywallet.dto.GetWalletResponse;

import java.util.UUID;

public interface WalletService {
  CreateWalletResponse createWallet(CreateWalletRequest createWalletRequest);

  GetWalletResponse getWallet(UUID walletId);
}
