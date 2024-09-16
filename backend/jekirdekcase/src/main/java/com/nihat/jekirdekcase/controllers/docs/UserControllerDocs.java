package com.nihat.jekirdekcase.controllers.docs;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.ErrorResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "Operations related to managing users")
@Validated
public interface UserControllerDocs {

    @Operation(
            summary = "Create a new user",
            description = "This endpoint allows you to create a new user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User creation request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateUserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    ResponseEntity<CreateUserResponse> createUser(@Valid  @RequestBody CreateUserRequest createUserRequest);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Update an existing user",
            description = "This endpoint allows you to update an existing user.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "User ID",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest, @PathVariable Long id);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Delete a user",
            description = "This endpoint allows you to delete a user.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "User ID",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get a user by ID",
            description = "This endpoint allows you to retrieve a user by their ID.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "User ID",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetUserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get all users",
            description = "This endpoint allows you to retrieve all users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetUserResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<GetUserResponse>> getAllUsers();
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get all users with pagination",
            description = "This endpoint allows you to retrieve all users with pagination.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetUserResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/page")
    ResponseEntity<Page<GetUserResponse>> getAllUsers(Pageable pageable);
    // -----------------------------------------------------------------------------------------------------------------

}
