package com.example.mywallet.exception;

import com.example.mywallet.dto.FieldErrorDetail;
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

import java.util.List;

import static java.util.Objects.requireNonNullElse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String PROPERTY_ERRORS = "errors";

  private final MessageResolver messageResolver;

  public GlobalExceptionHandler(MessageResolver messageResolver) {
    this.messageResolver = messageResolver;
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
    var problemDetail = ProblemDetail.forStatus(ex.getStatusCode());
    problemDetail.setTitle(messageResolver.resolve("business.error.title"));
    problemDetail.setDetail(messageResolver.resolve(
        requireNonNullElse(ex.getReason(), "generic.error")));
    return problemDetail;
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {
    var problemDetail = ex.getBody();
    problemDetail.setTitle(messageResolver.resolve("validation.error.title"));
    problemDetail.setDetail(messageResolver.resolve("validation.error"));
    List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(FieldErrorDetail::from)
        .toList();
    problemDetail.setProperty(PROPERTY_ERRORS, fieldErrors);
    return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
  }
}
