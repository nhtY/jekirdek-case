package com.nihat.jekirdekcase.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard error response format")
public record ErrorResponse(
        @Schema(description = "Timestamp of the error occurrence", example = "2024-09-09T12:00:00Z")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "HTTP status reason phrase", example = "Not Found")
        String error,

        @Schema(description = "Detailed error message", example = "Resource not found")
        String message
) {}
