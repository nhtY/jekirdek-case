package com.nihat.jekirdekcase.dtos.requests;

public record CreateCustomerRequest(
        String firstName,
        String lastName,
        String email,
        String region
) {
}
