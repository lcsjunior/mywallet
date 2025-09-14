package com.example.mywallet.dto;

import java.time.Instant;

public record ApiErrorResponse(
    String error,
    String message,
    int status,
    String path,
    Instant timestamp
) {
}