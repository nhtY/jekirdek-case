package com.nihat.jekirdekcase.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String firstName,
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String lastName,
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String username,
        @NotNull @Email
        String email,
        @NotNull @NotBlank @Size(min = 6, max = 50)
        String password,
        @NotNull @NotBlank
        String roleName) {
}
