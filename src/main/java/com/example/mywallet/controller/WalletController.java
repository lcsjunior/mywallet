package com.example.mywallet.controller;

import com.example.mywallet.dto.CreateWalletRequest;
import com.example.mywallet.dto.CreateWalletResponse;
import com.example.mywallet.dto.GetWalletResponse;
import com.example.mywallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  private final WalletService walletService;

  public WalletController(WalletService walletService) {
    this.walletService = walletService;
  }

  @PostMapping
  public ResponseEntity<CreateWalletResponse> createWallet(
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
}
