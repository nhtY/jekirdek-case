package com.nihat.jekirdekcase.dtos.requests;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String email,
        String username
) {
}
