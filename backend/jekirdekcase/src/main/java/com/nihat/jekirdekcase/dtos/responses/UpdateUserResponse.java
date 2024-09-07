package com.nihat.jekirdekcase.dtos.responses;

import java.time.LocalDateTime;

public record UpdateUserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        LocalDateTime updatedAt
) {
}
