package com.example.mywallet.dto;

import org.springframework.validation.FieldError;

public record FieldErrorDetail(
    String field,
    String error
) {
  public static FieldErrorDetail from(FieldError err) {
    return new FieldErrorDetail(err.getField(), err.getDefaultMessage());
  }
}