package com.example.mywallet.exception;

public enum ErrorMessages {

  GENERIC_ERROR("ERR-0001", "An unexpected error occurred"),
  VALIDATION_ERROR("ERR-0002", "Validation error"),
  WALLET_NOT_FOUND("ERR-1001", "Wallet not found");

  private final String code;

  private final String message;

  ErrorMessages(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }
}
