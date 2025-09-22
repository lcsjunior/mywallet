package com.example.mywallet.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public enum ErrorMessages {

  GENERIC_ERROR("An unexpected error occurred", INTERNAL_SERVER_ERROR),
  VALIDATION_ERROR("Validation error", BAD_REQUEST),
  WALLET_NOT_FOUND("Wallet not found", NOT_FOUND),
  INSUFFICIENT_FUNDS("Insufficient funds", UNPROCESSABLE_ENTITY);

  private final String message;
  private final HttpStatus status;

  ErrorMessages(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }

  public HttpStatus getStatus() {
    return status;
  }
}
