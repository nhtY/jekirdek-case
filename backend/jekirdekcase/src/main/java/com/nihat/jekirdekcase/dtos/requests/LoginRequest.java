package com.nihat.jekirdekcase.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull @NotBlank
        String email,
        @NotNull @NotBlank
        String password) {}