package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.DepositRequest;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.dto.WithdrawRequest;
import com.example.mywallet.mapper.CreateWalletMapper;
import com.example.mywallet.mapper.GetWalletMapper;
import com.example.mywallet.model.Wallet;
import com.example.mywallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

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
    var newWallet = CreateWalletMapper.toModel(createWalletRequest);
    validateWalletNotExists(newWallet);
    walletRepository.saveAndFlush(newWallet);
    log.info("Wallet created. userId={}, walletId={}", newWallet.getUserId(), newWallet.getId());
    return GetWalletMapper.toResponse(newWallet);
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
          return new ResponseStatusException(NOT_FOUND, "wallet.not.found");
        });
  }

  private void validateWalletNotExists(Wallet newWallet) {
    var walletOpt = walletRepository.findByUserId(newWallet.getUserId());
    if (walletOpt.isPresent()) {
      var wallet = walletOpt.get();
      log.warn("Wallet already exists. userId={}, walletId={}", wallet.getUserId(), wallet.getId());
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "wallet.already.exists");
    }
  }

  private void validateSufficientFunds(Wallet wallet, BigDecimal amount) {
    var currentBalance = wallet.getBalance();
    if (currentBalance.compareTo(amount) < 0) {
      log.warn("Insufficient funds. walletId={}, amount={}, balance={}", wallet.getId(), amount, currentBalance);
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "insufficient.funds");
    }
  }
}
