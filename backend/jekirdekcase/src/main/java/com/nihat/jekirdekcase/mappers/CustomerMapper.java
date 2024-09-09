package com.nihat.jekirdekcase.mappers;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.entities.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {

    Customer mapToCustomer(CreateCustomerRequest createCustomerRequest);
    Customer mapToCustomer(UpdateCustomerRequest updateCustomerRequest);

    CreateCustomerResponse mapToCreateCustomerResponse(Customer customer);
    UpdateCustomerResponse mapToUpdateCustomerResponse(Customer customer);
    GetCustomerResponse mapToGetCustomerResponse(Customer customer);
    List<GetCustomerResponse> mapToGetCustomerResponseList(Iterable<Customer> customers);

}
