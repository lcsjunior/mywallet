package com.example.mywallet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Wallet!";
    }
}
