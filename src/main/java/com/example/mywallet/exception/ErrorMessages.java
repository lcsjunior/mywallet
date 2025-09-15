package com.example.mywallet.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorMessages {

  GENERIC_ERROR("ERR-0001", "An unexpected error occurred", INTERNAL_SERVER_ERROR),
  VALIDATION_ERROR("ERR-0002", "Validation error", BAD_REQUEST),
  WALLET_NOT_FOUND("ERR-1001", "Wallet not found", NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;

  ErrorMessages(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }

  public HttpStatus getStatus() {
    return status;
  }
}
