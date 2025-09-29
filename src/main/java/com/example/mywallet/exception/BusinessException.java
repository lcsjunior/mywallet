package com.example.mywallet.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class BusinessException extends ResponseStatusException {
  public BusinessException(String reason) {
    super(UNPROCESSABLE_ENTITY, reason);
  }
}
