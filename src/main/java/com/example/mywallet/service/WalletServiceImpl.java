package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.CreateWalletResponse;
import com.example.mywallet.dto.DepositRequest;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.dto.WithdrawRequest;
import com.example.mywallet.exception.ApiException;
import com.example.mywallet.mapper.CreateWalletMapper;
import com.example.mywallet.mapper.GetWalletMapper;
import com.example.mywallet.model.Wallet;
import com.example.mywallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.mywallet.exception.ErrorMessages.INSUFFICIENT_FUNDS;
import static com.example.mywallet.exception.ErrorMessages.WALLET_NOT_FOUND;

@Service
public class WalletServiceImpl implements WalletService {

  private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

  private final WalletRepository walletRepository;

  public WalletServiceImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Transactional
  @Override
  public CreateWalletResponse createWallet(CreateWalletRequest createWalletRequest) {
    var wallet = new Wallet();
    wallet.setUserId(createWalletRequest.userId());
    walletRepository.save(wallet);
    log.info("Wallet created. userId={}, walletId={}", createWalletRequest.userId(), wallet.getId());
    return CreateWalletMapper.toResponse(wallet);
  }

  @Override
  public GetWalletResponse getWallet(UUID walletId) {
    var wallet = retrieveWallet(walletId);
    return GetWalletMapper.toResponse(wallet);
  }

  @Override
  public void deposit(UUID walletId, DepositRequest depositRequest) {
    var wallet = retrieveWallet(walletId);
    var amount = depositRequest.amount();
    var currentBalance = wallet.getBalance();
    wallet.setBalance(currentBalance.add(amount));
    walletRepository.save(wallet);
    log.info("Deposit successful. walletId={}, amount={}, previousBalance={}, newBalance={}",
        walletId, amount, currentBalance, wallet.getBalance());
  }

  @Override
  public void withdraw(UUID walletId, WithdrawRequest withdrawRequest) {
    var wallet = retrieveWallet(walletId);
    var amount = withdrawRequest.amount();
    var currentBalance = wallet.getBalance();
    if (currentBalance.compareTo(amount) < 0) {
      log.warn("Withdraw failed: insufficient funds. walletId={}, amount={}, balance={}", walletId, amount, currentBalance);
      throw new ApiException(INSUFFICIENT_FUNDS);
    }
    wallet.setBalance(currentBalance.subtract(amount));
    walletRepository.save(wallet);
    log.info("Withdraw successful. walletId={}, amount={}, previousBalance={}, newBalance={}",
        walletId, amount, currentBalance, wallet.getBalance());
  }

  private Wallet retrieveWallet(UUID walletId) {
    return walletRepository.findById(walletId)
        .orElseThrow(() -> {
          log.warn("Wallet not found for id={}", walletId);
          return new ApiException(WALLET_NOT_FOUND);
        });
  }
}
