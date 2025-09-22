package com.example.mywallet.dto;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
    String name,
    String message,
    int status,
    String path,
    Instant timestamp,
    List<FieldErrorDetail> details
) {
}