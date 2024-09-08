package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.exceptions.AlreadyExistsException;
import com.nihat.jekirdekcase.exceptions.ResourceNotFoundException;
import com.nihat.jekirdekcase.mappers.CustomerMapper;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import com.nihat.jekirdekcase.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            return customerMapper.mapToCreateCustomerResponse(
                    customerRepository.save(customerMapper.mapToCustomer(createCustomerRequest))
            );
        } catch (DataIntegrityViolationException e) {
            AlreadyExistsException ex = throwAlreadyExistsExceptionForCustomerCreation(createCustomerRequest.email());
            if (ex != null) {
                throw ex;
            } else {
                throw  e;
            }
        }
    }

    private AlreadyExistsException throwAlreadyExistsExceptionForCustomerCreation(String email) {
        if (customerRepository.existsByEmail(email)) {
            return new AlreadyExistsException("Customer with this email: " + email + " already exists.");
        }
        return null;
    }

    @Override
    public UpdateCustomerResponse updateCustomer(UpdateCustomerRequest updateCustomerRequest, Long id) {
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found.");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public GetCustomerResponse getCustomer(Long id) {
        return  customerRepository.findById(id)
                .map(customerMapper::mapToGetCustomerResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found."));
    }

    @Override
    public List<GetCustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::mapToGetCustomerResponse)
                .toList();
    }

    @Override
    public Page<GetCustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::mapToGetCustomerResponse);
    }
}
