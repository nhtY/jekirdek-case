package com.nihat.jekirdekcase.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;

public record GetUserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        List<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
