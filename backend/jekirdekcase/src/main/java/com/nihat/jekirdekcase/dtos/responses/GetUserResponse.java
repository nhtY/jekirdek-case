package com.nihat.jekirdekcase.dtos.responses;

public record GetUserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String createdAt,
        String updatedAt
) {
}
