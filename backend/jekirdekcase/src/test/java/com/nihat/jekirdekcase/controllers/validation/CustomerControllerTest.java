package com.nihat.jekirdekcase.controllers.validation;

import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void whenCreateCustomerWithInvalidFirstName_thenServiceMethodNotCalled() throws Exception {
        String request = """
            {
                "firstName": "",
                "lastName": "Doe",
                "email": "john@example.com",
                "region": "US"
            }
        """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());


        verify(customerService, never()).saveCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    public void whenUpdateCustomerWithInvalidEmail_thenServiceMethodNotCalled() throws Exception {
        String request = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "email": "invalid-email",
                "region": "US"
            }
        """;

        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).updateCustomer(any(UpdateCustomerRequest.class), anyLong());
    }
}
