package com.nihat.jekirdekcase.dtos.responses;

public record CreateUserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String createdAt) {
}
