package com.example.mywallet.service;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.CreateWalletResponse;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.exception.NotFoundException;
import com.example.mywallet.mapper.CreateWalletMapper;
import com.example.mywallet.mapper.GetWalletMapper;
import com.example.mywallet.model.Wallet;
import com.example.mywallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    return CreateWalletMapper.toResponse(wallet);
  }

  @Override
  public GetWalletResponse getWallet(UUID walletId) {
    var wallet = walletRepository.findById(walletId)
        .orElseThrow(() -> {
          log.warn("Wallet not found for id={}", walletId);
          return new NotFoundException(WALLET_NOT_FOUND);
        });
    return GetWalletMapper.toResponse(wallet);
  }
}
