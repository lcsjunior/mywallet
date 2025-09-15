package com.example.mywallet.exception;

import com.example.mywallet.dto.ApiErrorResponse;
import com.example.mywallet.dto.FieldErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static com.example.mywallet.exception.ErrorMessages.GENERIC_ERROR;
import static com.example.mywallet.exception.ErrorMessages.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
      ApiException ex,
      HttpServletRequest request) {

    ApiErrorResponse response = new ApiErrorResponse(
        ex.getError().getCode(),
        ex.getMessage(),
        ex.getStatus().value(),
        request.getRequestURI(),
        Instant.now(),
        null
    );
    return ResponseEntity.status(ex.getStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    var details = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(err -> new FieldErrorDetail(err.getField(), err.getDefaultMessage()))
        .toList();
    ApiErrorResponse response = new ApiErrorResponse(
        VALIDATION_ERROR.getCode(),
        VALIDATION_ERROR.formatMessage(),
        BAD_REQUEST.value(),
        request.getRequestURI(),
        Instant.now(),
        details
    );
    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(
      Exception ex,
      HttpServletRequest request) {

    ApiErrorResponse response = new ApiErrorResponse(
        GENERIC_ERROR.getCode(),
        GENERIC_ERROR.formatMessage(),
        NOT_FOUND.value(),
        request.getRequestURI(),
        Instant.now(),
        null
    );
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
  }
}
