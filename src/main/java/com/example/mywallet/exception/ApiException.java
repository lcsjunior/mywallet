package com.example.mywallet.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final HttpStatus status;
  private final ErrorMessages error;

  public ApiException(HttpStatus status, ErrorMessages error, String message) {
    super(message);
    this.status = status;
    this.error = error;
  }

  public ApiException(HttpStatus status, ErrorMessages error) {
    this(status, error, error.formatMessage());
  }

  public HttpStatus getStatus() {
    return status;
  }

  public ErrorMessages getError() {
    return error;
  }
}