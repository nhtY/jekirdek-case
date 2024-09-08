package com.nihat.jekirdekcase.dtos.responses;

import java.time.LocalDateTime;

public record UpdateCustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String region,
        LocalDateTime registrationDate,
        LocalDateTime updatedAt
) {
}
