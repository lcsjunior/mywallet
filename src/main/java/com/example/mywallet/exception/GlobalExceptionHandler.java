package com.example.mywallet.exception;

import com.example.mywallet.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static com.example.mywallet.exception.ErrorMessages.GENERIC_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
      NotFoundException ex,
      HttpServletRequest request) {

    ApiErrorResponse response = new ApiErrorResponse(
        NOT_FOUND.name(),
        ex.getMessage(),
        NOT_FOUND.value(),
        request.getRequestURI(),
        Instant.now()
    );
    return ResponseEntity.status(NOT_FOUND).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(
      Exception ex,
      HttpServletRequest request) {

    ApiErrorResponse response = new ApiErrorResponse(
        INTERNAL_SERVER_ERROR.name(),
        GENERIC_ERROR,
        INTERNAL_SERVER_ERROR.value(),
        request.getRequestURI(),
        Instant.now()
    );
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
  }
}
