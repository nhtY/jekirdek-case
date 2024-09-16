package com.nihat.jekirdekcase.services;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {
    CreateCustomerResponse saveCustomer(CreateCustomerRequest createCustomerRequest);
    UpdateCustomerResponse updateCustomer(UpdateCustomerRequest updateCustomerRequest, Long id);
    void deleteCustomer(Long id);
    GetCustomerResponse getCustomer(Long id);
    List<GetCustomerResponse> getAllCustomers();
    Page<GetCustomerResponse> getAllCustomers(Pageable pageable);

    Page<GetCustomerResponse> filterCustomersUsingSpecs(String firstName, String lastName, String email, String region,
                                                        LocalDate registrationDateStart, LocalDate registrationDateEnd,
                                                        Pageable pageable);

    Page<GetCustomerResponse> filterCustomersUsingStream(String firstName, String lastName, String email, String region,
                                              LocalDate registrationDateStart, LocalDate registrationDateEnd,
                                              Pageable pageable);
    }
