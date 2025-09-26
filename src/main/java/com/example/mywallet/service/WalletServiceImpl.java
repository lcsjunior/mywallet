package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
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

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.mywallet.exception.ErrorMessages.INSUFFICIENT_FUNDS;
import static com.example.mywallet.exception.ErrorMessages.WALLET_ALREADY_EXISTS;
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
  public GetWalletResponse createWallet(CreateWalletRequest createWalletRequest) {
    var wallet = CreateWalletMapper.toModel(createWalletRequest);
    validateWalletNotExists(wallet);
    walletRepository.saveAndFlush(wallet);
    log.info("Wallet created. walletId={}, userId={}, type={}", wallet.getId(), wallet.getUserId(), wallet.getType());
    return GetWalletMapper.toResponse(wallet);
  }

  @Override
  public GetWalletResponse getWallet(UUID walletId) {
    var wallet = retrieveWallet(walletId);
    return GetWalletMapper.toResponse(wallet);
  }

  @Transactional
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

  @Transactional
  @Override
  public void withdraw(UUID walletId, WithdrawRequest withdrawRequest) {
    var wallet = retrieveWallet(walletId);
    var amount = withdrawRequest.amount();
    var currentBalance = wallet.getBalance();
    validateSufficientFunds(wallet, amount);
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

  private void validateWalletNotExists(Wallet wallet) {
    var wallets = walletRepository.findAllByUserId(wallet.getUserId());
    boolean walletExists = wallets.stream()
        .anyMatch(w -> w.getType() == wallet.getType());
    if (walletExists) {
      log.warn("Uniqueness constraint violation. userId={}, type={}", wallet.getUserId(), wallet.getType());
      throw new ApiException(WALLET_ALREADY_EXISTS);
    }
  }

  private void validateSufficientFunds(Wallet wallet, BigDecimal amount) {
    var currentBalance = wallet.getBalance();
    if (currentBalance.compareTo(amount) < 0) {
      log.warn("Insufficient funds. walletId={}, amount={}, balance={}", wallet.getId(), amount, currentBalance);
      throw new ApiException(INSUFFICIENT_FUNDS);
    }
  }
}
