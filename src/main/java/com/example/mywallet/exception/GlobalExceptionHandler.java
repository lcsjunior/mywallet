package com.example.mywallet.exception;

import com.example.mywallet.resolver.MessageResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageResolver messageResolver;

  public GlobalExceptionHandler(MessageResolver messageResolver) {
    this.messageResolver = messageResolver;
  }

  private static final String PROPERTY_ERRORS = "errors";
  private static final String PROPERTY_TIMESTAMP = "timestamp";

  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatusCode());
    problemDetail.setTitle(messageResolver.resolve("business.error.title"));
    problemDetail.setDetail(messageResolver.resolve(
        requireNonNullElse(ex.getReason(), "generic.error")));
    problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now());
    return problemDetail;
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {
    ProblemDetail problemDetail = ex.getBody();
    problemDetail.setTitle(messageResolver.resolve("validation.error.title"));
    problemDetail.setDetail(messageResolver.resolve("validation.error"));
    List<String> fieldErrors = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .toList();
    problemDetail.setProperty(PROPERTY_ERRORS, fieldErrors);
    problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now());
    return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
  }
}
