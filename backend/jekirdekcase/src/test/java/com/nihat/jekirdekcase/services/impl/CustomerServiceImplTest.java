package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.entities.Customer;
import com.nihat.jekirdekcase.exceptions.AlreadyExistsException;
import com.nihat.jekirdekcase.exceptions.ResourceNotFoundException;
import com.nihat.jekirdekcase.mappers.CustomerMapper;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CreateCustomerRequest createCustomerRequest;
    private UpdateCustomerRequest updateCustomerRequest;
    private CreateCustomerResponse createCustomerResponse;
    private UpdateCustomerResponse updateCustomerResponse;
    private GetCustomerResponse getCustomerResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmail("jane.doe@example.com");
        customer.setRegion("Ankara");

        createCustomerRequest = new CreateCustomerRequest("Jane", "Doe", "jane.doe@example.com", "Ankara");
        updateCustomerRequest = new UpdateCustomerRequest("Jane", "Doe", "jane.doe@example.com", "Ankara");
        createCustomerResponse = new CreateCustomerResponse(1L, "Jane", "Doe", "jane.doe@example.com", "Ankara", customer.getRegistrationDate());
        updateCustomerResponse = new UpdateCustomerResponse(1L, "Jane", "Doe", "jane.doe@example.com", "Ankara", customer.getRegistrationDate(), customer.getUpdatedAt());
        getCustomerResponse = new GetCustomerResponse(1L, "Jane", "Doe", "jane.doe@example.com", "Ankara", customer.getRegistrationDate(), customer.getUpdatedAt());
    }

    @Test
    void saveCustomer_ShouldReturnCreateCustomerResponse() {
        when(customerMapper.mapToCustomer(createCustomerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.mapToCreateCustomerResponse(customer)).thenReturn(createCustomerResponse);

        CreateCustomerResponse response = customerService.saveCustomer(createCustomerRequest);

        assertThat(response).isEqualTo(createCustomerResponse);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void saveCustomer_ShouldThrowAlreadyExistsExceptionIfEmailExists() {
        when(customerMapper.mapToCustomer(createCustomerRequest)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenThrow(DataIntegrityViolationException.class);
        when(customerRepository.existsByEmail(any(String.class))).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> customerService.saveCustomer(createCustomerRequest));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldThrowExceptionIfCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(1L));
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateCustomer_ShouldReturnUpdateCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.mapToCustomer(updateCustomerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.mapToUpdateCustomerResponse(customer)).thenReturn(updateCustomerResponse);

        UpdateCustomerResponse response = customerService.updateCustomer(updateCustomerRequest, 1L);

        assertThat(response).isEqualTo(updateCustomerResponse);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_ShouldThrowExceptionIfCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(updateCustomerRequest, 1L));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_shouldThrowAlreadyExistsException_whenEmailAlreadyExists() {
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("Jane", "Doe", "new.email@example.com", "Ankara");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail("new.email@example.com")).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            customerService.updateCustomer(updateCustomerRequest, 1L);
        });

        assertEquals("Customer with this email already exists.", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomer_ShouldReturnGetCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.mapToGetCustomerResponse(customer)).thenReturn(getCustomerResponse);

        GetCustomerResponse response = customerService.getCustomer(1L);

        assertThat(response).isEqualTo(getCustomerResponse);
    }

    @Test
    void getCustomer_ShouldThrowExceptionIfCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(1L));
    }

    @Test
    void getAllCustomers_ShouldReturnListOfGetCustomerResponses() {
        List<Customer> customers = List.of(customer);
        List<GetCustomerResponse> customerResponses = List.of(getCustomerResponse);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.mapToGetCustomerResponse(any(Customer.class))).thenReturn(getCustomerResponse);

        List<GetCustomerResponse> responses = customerService.getAllCustomers();

        assertThat(responses).isEqualTo(customerResponses);
    }

    @Test
    void getAllCustomersWithPagination_ShouldReturnPageOfGetCustomerResponses() {
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers);
        List<GetCustomerResponse> customerResponses = List.of(getCustomerResponse);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.mapToGetCustomerResponse(customer)).thenReturn(getCustomerResponse);

        Page<GetCustomerResponse> responsePage = customerService.getAllCustomers(Pageable.unpaged());

        assertThat(responsePage.getContent()).isEqualTo(customerResponses);
    }
}
