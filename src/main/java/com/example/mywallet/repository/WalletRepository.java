package com.example.mywallet.repository;

import com.example.mywallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
  List<Wallet> findAllByUserId(UUID userId);
}
