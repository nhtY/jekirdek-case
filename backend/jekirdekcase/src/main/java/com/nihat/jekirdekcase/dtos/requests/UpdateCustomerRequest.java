package com.nihat.jekirdekcase.dtos.requests;

public record UpdateCustomerRequest(
        String firstName,
        String lastName,
        String email,
        String region
) {
}
