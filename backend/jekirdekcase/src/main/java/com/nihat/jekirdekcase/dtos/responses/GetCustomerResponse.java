package com.nihat.jekirdekcase.dtos.responses;

public record GetCustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String region,
        String registrationDate,
        String updatedAt
) {
}
