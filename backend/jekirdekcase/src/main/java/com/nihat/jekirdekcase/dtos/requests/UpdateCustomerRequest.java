package com.nihat.jekirdekcase.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String firstName,
        @NotNull @NotBlank @Size(min = 3, max = 50)
        String lastName,
        @NotNull @Email
        String email,
        @NotNull @NotBlank
        String region
) {
}
