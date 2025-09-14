package com.example.mywallet.exception;

public class NotFoundException extends RuntimeException {
  private final ErrorMessages error;

  public NotFoundException(ErrorMessages error, String message) {
    super(message);
    this.error = error;
  }

  public NotFoundException(ErrorMessages error) {
    super(error.formatMessage());
    this.error = error;
  }

  public ErrorMessages getError() {
    return error;
  }
}