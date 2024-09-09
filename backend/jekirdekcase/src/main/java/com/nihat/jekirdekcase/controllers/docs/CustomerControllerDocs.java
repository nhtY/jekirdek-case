package com.nihat.jekirdekcase.controllers.docs;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.ErrorResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Management", description = "Operations related to managing customers")
public interface CustomerControllerDocs {

    @Operation(
            summary = "Create a new customer",
            description = "This endpoint allows you to create a new customer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer creation request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateCustomerRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Customer created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateCustomerResponse.class)
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
    ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Update an existing customer",
            description = "This endpoint allows you to update an existing customer.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "Customer ID",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer update request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCustomerRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateCustomerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest, @PathVariable Long id);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Delete a customer",
            description = "This endpoint allows you to delete a customer.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "Customer ID",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Customer deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCustomer(@PathVariable Long id);
    // ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get a customer by ID",
            description = "This endpoint allows you to retrieve a customer by their ID.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "Customer ID",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetCustomerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<GetCustomerResponse> getCustomerById(@PathVariable Long id);
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get all customers",
            description = "This endpoint allows you to retrieve all customers.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customers retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetCustomerResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<GetCustomerResponse>> getAllCustomers();
    // -----------------------------------------------------------------------------------------------------------------

    @Operation(
            summary = "Get all customers with pagination",
            description = "This endpoint allows you to retrieve all customers with pagination.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customers retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetCustomerResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/page")
    ResponseEntity<Page<GetCustomerResponse>> getAllCustomers(Pageable pageable);
    // -----------------------------------------------------------------------------------------------------------------

}
