package com.example.mywallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {

  public ApiException(HttpStatus status, String reason) {
    super(status, reason);
  }
}