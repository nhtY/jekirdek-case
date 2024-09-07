package com.nihat.jekirdekcase.dtos.requests;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password) {
}
