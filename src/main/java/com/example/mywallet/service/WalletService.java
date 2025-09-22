package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.CreateWalletResponse;
import com.example.mywallet.dto.DepositRequest;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.dto.WithdrawRequest;

import java.util.UUID;

public interface WalletService {
  CreateWalletResponse createWallet(CreateWalletRequest createWalletRequest);

  GetWalletResponse getWallet(UUID walletId);

  void deposit(UUID walletId, DepositRequest depositRequest);

  void withdraw(UUID walletId, WithdrawRequest withdrawRequest);
}
