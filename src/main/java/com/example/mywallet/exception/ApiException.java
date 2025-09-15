package com.example.mywallet.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final ErrorMessages error;

  public ApiException(ErrorMessages error, String message) {
    super(message);
    this.error = error;
  }

  public ApiException(ErrorMessages error) {
    this(error, error.formatMessage());
  }

  public ErrorMessages getError() {
    return error;
  }

  public HttpStatus getStatus() {
    return error.getStatus();
  }
}