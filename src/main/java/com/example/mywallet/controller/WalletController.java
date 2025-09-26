package com.example.mywallet.controller;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.DepositRequest;
import com.example.mywallet.dto.WithdrawRequest;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/v1/wallets")
public class WalletController {

  private final WalletService walletService;

  public WalletController(WalletService walletService) {
    this.walletService = walletService;
  }

  @PostMapping
  public ResponseEntity<GetWalletResponse> createWallet(
      @Valid @RequestBody CreateWalletRequest createWalletRequest,
      UriComponentsBuilder uriBuilder) {

    var createWalletResponse = walletService.createWallet(createWalletRequest);
    var location = uriBuilder
        .path("/wallets/{id}")
        .buildAndExpand(createWalletResponse.walletId())
        .toUri();
    return ResponseEntity
        .created(location)
        .body(createWalletResponse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetWalletResponse> getWallet(
      @PathVariable("id") UUID walletId) {

    var getWalletResponse = walletService.getWallet(walletId);
    return ResponseEntity.ok(getWalletResponse);
  }

  @PostMapping("/{id}/deposit")
  public ResponseEntity<Void> deposit(
      @PathVariable("id") UUID walletId,
      @Valid @RequestBody DepositRequest depositRequest) {

    walletService.deposit(walletId, depositRequest);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/withdraw")
  public ResponseEntity<Void> withdraw(
      @PathVariable("id") UUID walletId,
      @Valid @RequestBody WithdrawRequest withdrawRequest) {

    walletService.withdraw(walletId, withdrawRequest);
    return ResponseEntity.noContent().build();
  }
}
