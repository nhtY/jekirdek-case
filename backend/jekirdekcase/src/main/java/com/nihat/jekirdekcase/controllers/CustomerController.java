package com.nihat.jekirdekcase.controllers;

import com.nihat.jekirdekcase.controllers.docs.CustomerControllerDocs;
import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerDocs {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<CreateCustomerResponse> createCustomer(CreateCustomerRequest createCustomerRequest) {
        return ResponseEntity.ok(customerService.saveCustomer(createCustomerRequest));
    }

    @Override
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(UpdateCustomerRequest updateCustomerRequest, Long id) {
        return ResponseEntity.ok(customerService.updateCustomer(updateCustomerRequest, id));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetCustomerResponse> getCustomerById(Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @Override
    public ResponseEntity<List<GetCustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Override
    public ResponseEntity<Page<GetCustomerResponse>> getAllCustomers(Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }
}