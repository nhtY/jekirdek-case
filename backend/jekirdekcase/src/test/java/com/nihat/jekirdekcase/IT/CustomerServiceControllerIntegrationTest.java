package com.nihat.jekirdekcase.IT;

import com.nihat.jekirdekcase.entities.Customer;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import com.nihat.jekirdekcase.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        // Insert 100 customers
        List<Customer> customers = IntStream.range(0, 100)
                .mapToObj(i -> Customer.builder()
                        .firstName("FirstName" + i)
                        .lastName("LastName" + i)
                        .email("email" + i + "@test.com")
                        .region(i % 2 == 0 ? "RegionA" : "RegionB")
                        .registrationDate(LocalDateTime.now().minusDays(i))
                        .build())
                .collect(Collectors.toList());
        customerRepository.saveAll(customers);
    }

    @Test
    @Transactional
    public void testFilterCustomersViaController() throws Exception {
        // Define the filter parameters
        String url = "/api/v1/customers/filter?firstName=FirstName1";

        // Perform the request and get the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Process the response body
        String responseBody = result.getResponse().getContentAsString();

        // Assert the response contains filtered data
        assertThat(responseBody).contains("\"firstName\":\"FirstName1\"");
        assertThat(responseBody).contains("\"region\":\"RegionB\"");
    }

    @Test
    @Transactional
    public void testFilterCustomersNoResult() throws Exception {
        // Define the filter parameters for a non-existing customer
        String url = "/api/v1/customers/filter?firstName=NonExistingName&region=NonExistingRegion&registrationDateStart="
                + LocalDate.now().minusDays(30).toString()
                + "&registrationDateEnd=" + LocalDate.now().toString();

        // Perform the request and get the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Process the response body
        String responseBody = result.getResponse().getContentAsString();

        // Assert the response body is empty or indicates no results
        assertThat(responseBody).isEmpty();  // Adjust if your no-result response format is different
    }

    @Test
    @Transactional
    public void testFilterCustomersCountViaController() throws Exception {
        String url = "/api/v1/customers/filter?firstName=FirstName1&region=RegionB&registrationDateStart="
                + LocalDate.now().minusDays(30).toString()
                + "&registrationDateEnd=" + LocalDate.now().toString();

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    // Count the number of JSON objects in the response
                    long count = response.lines().count();
                    assertThat(count).isEqualTo(1);
                });
    }
}