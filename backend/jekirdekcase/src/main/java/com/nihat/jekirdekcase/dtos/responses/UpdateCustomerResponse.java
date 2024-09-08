package com.nihat.jekirdekcase.dtos.responses;

public record UpdateCustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String region,
        String registrationDate,
        String updatedAt
) {
}
