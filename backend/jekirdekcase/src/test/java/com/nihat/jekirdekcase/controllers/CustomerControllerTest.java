package com.nihat.jekirdekcase.controllers;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("John", "Doe", "john.doe@example.com", "Europe");
        CreateCustomerResponse response = new CreateCustomerResponse(1L, "John", "Doe", "john.doe@example.com", "Europe", LocalDateTime.now());

        when(customerService.saveCustomer(any(CreateCustomerRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"email\":\"john.doe@example.com\", \"region\":\"Europe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.region").value("Europe"));

        verify(customerService, times(1)).saveCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        UpdateCustomerRequest request = new UpdateCustomerRequest("Jane", "Doe", "jane.doe@example.com", "Asia");
        UpdateCustomerResponse response = new UpdateCustomerResponse(1L, "Jane", "Doe", "jane.doe@example.com", "Asia", LocalDateTime.now(), LocalDateTime.now());

        when(customerService.updateCustomer(any(UpdateCustomerRequest.class), anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Jane\", \"lastName\":\"Doe\", \"email\":\"jane.doe@example.com\", \"region\":\"Asia\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.region").value("Asia"));

        verify(customerService, times(1)).updateCustomer(any(UpdateCustomerRequest.class), anyLong());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(anyLong());
    }

    @Test
    void testGetCustomerById() throws Exception {
        GetCustomerResponse response = new GetCustomerResponse(1L, "John", "Doe", "john.doe@example.com", "Europe", LocalDateTime.now(), LocalDateTime.now());

        when(customerService.getCustomer(anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.region").value("Europe"));

        verify(customerService, times(1)).getCustomer(anyLong());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        GetCustomerResponse customer1 = new GetCustomerResponse(1L, "John", "Doe", "john.doe@example.com", "Europe", LocalDateTime.now(), LocalDateTime.now());
        GetCustomerResponse customer2 = new GetCustomerResponse(2L, "Jane", "Doe", "jane.doe@example.com", "Asia", LocalDateTime.now(), LocalDateTime.now());
        List<GetCustomerResponse> customerList = List.of(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void testGetAllCustomersWithPagination() throws Exception {
        GetCustomerResponse customer1 = new GetCustomerResponse(1L, "John", "Doe", "john.doe@example.com", "Europe", LocalDateTime.now(), LocalDateTime.now());
        GetCustomerResponse customer2 = new GetCustomerResponse(2L, "Jane", "Doe", "jane.doe@example.com", "Asia", LocalDateTime.now(), LocalDateTime.now());
        Page<GetCustomerResponse> customerPage = new PageImpl<>(List.of(customer1, customer2));

        when(customerService.getAllCustomers(any(Pageable.class))).thenReturn(customerPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/page")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.content[1].lastName").value("Doe"));

        verify(customerService, times(1)).getAllCustomers(any(Pageable.class));
    }
}