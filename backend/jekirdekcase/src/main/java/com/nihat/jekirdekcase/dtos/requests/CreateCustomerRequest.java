package com.nihat.jekirdekcase.dtos.requests;

import jakarta.validation.constraints.*;

public record CreateCustomerRequest(
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String firstName,
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String lastName,
        @NotNull @NotBlank @Email
        String email,
        @NotNull @NotBlank
        String region
) {
}
