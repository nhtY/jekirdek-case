package com.nihat.jekirdekcase.dtos.responses;

import java.time.LocalDateTime;

public record GetUserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
