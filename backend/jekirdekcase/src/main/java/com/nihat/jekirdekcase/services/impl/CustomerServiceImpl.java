package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.mappers.CustomerMapper;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import com.nihat.jekirdekcase.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CreateCustomerResponse saveCustomer(CreateCustomerRequest createCustomerRequest) {
        return null;
    }

    @Override
    public UpdateCustomerResponse updateCustomer(UpdateCustomerRequest updateCustomerRequest) {
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {

    }

    @Override
    public GetCustomerResponse getCustomer(Long id) {
        return null;
    }

    @Override
    public List<GetCustomerResponse> getAllCustomers() {
        return List.of();
    }

    @Override
    public Page<GetCustomerResponse> getAllCustomers(Pageable pageable) {
        return null;
    }
}
