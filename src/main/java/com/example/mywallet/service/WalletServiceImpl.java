package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.DepositRequest;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.dto.WithdrawRequest;
import com.example.mywallet.exception.BusinessException;
import com.example.mywallet.exception.NotFoundException;
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

@Service
public class WalletServiceImpl implements WalletService {

  private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

  private static final String LOG_PREFIX = "[WALLET_SERVICE] ";

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
    log.info(LOG_PREFIX + "Wallet created | userId={}, walletId={}", newWallet.getUserId(), newWallet.getId());
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
    log.info(LOG_PREFIX + "Deposit successful | walletId={}, amount={}, previousBalance={}, newBalance={}",
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
    log.info(LOG_PREFIX + "Withdraw successful | walletId={}, amount={}, previousBalance={}, newBalance={}",
        walletId, amount, currentBalance, wallet.getBalance());
  }

  private Wallet retrieveWallet(UUID walletId) {
    return walletRepository.findById(walletId)
        .orElseThrow(() -> {
          log.warn(LOG_PREFIX + "Wallet not found | walletId={}", walletId);
          return new NotFoundException("wallet.not.found");
        });
  }

  private void validateWalletNotExists(Wallet newWallet) {
    var walletOpt = walletRepository.findByUserId(newWallet.getUserId());
    if (walletOpt.isPresent()) {
      var wallet = walletOpt.get();
      log.warn(LOG_PREFIX + "Wallet already exists | userId={}, walletId={}", wallet.getUserId(), wallet.getId());
      throw new BusinessException("wallet.already.exists");
    }
  }

  private void validateSufficientFunds(Wallet wallet, BigDecimal amount) {
    var currentBalance = wallet.getBalance();
    if (currentBalance.compareTo(amount) < 0) {
      log.warn(LOG_PREFIX + "Insufficient funds | walletId={}, amount={}, balance={}", wallet.getId(), amount, currentBalance);
      throw new BusinessException("wallet.insufficient.funds");
    }
  }
}
