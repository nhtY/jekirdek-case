package com.nihat.jekirdekcase.controllers.docs;

import com.nihat.jekirdekcase.dtos.requests.LoginRequest;
import com.nihat.jekirdekcase.dtos.responses.CurrentUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Authentication")
public interface AuthControllerDocs {

    @Operation(
            summary = "Login to the application",
            description = "Login to the application with email and password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Successful login",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials"
                    )
            }
    )
    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, @RequestHeader("device-os") String deviceOs);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Logout from the application",
            description = "Logout from the application",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Successful logout"
                    )
            }
    )
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get current user",
            description = "Get the current user",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Current user information",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CurrentUserResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @GetMapping("/current-user")
    ResponseEntity<CurrentUserResponse> getCurrentUser(HttpServletRequest request);
}
