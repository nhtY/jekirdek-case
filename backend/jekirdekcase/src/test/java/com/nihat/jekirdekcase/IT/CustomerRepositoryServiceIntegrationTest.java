package com.nihat.jekirdekcase.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.entities.Customer;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import com.nihat.jekirdekcase.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomerRepositoryServiceIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testStreamFilteredCustomersByFirstNameAndRegion() throws IOException {
        // Set up filtering criteria
        String firstName = "FirstName1";
        String lastName = null;
        String email = null;
        String region = "RegionB";
        LocalDate registrationDateStart = LocalDate.now().minusDays(30);
        LocalDate registrationDateEnd = LocalDate.now();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Stream filtered customers
        customerService.streamFilteredCustomers(firstName, lastName, email, region, registrationDateStart, registrationDateEnd, "id", "asc", outputStream);

        String output = outputStream.toString();
        // Assert that filtered customers are correctly written to the output stream
        assertThat(output).contains("\"firstName\":\"FirstName1\"");
        assertThat(output).contains("\"region\":\"RegionB\"");
    }

    @Test
    @Transactional
    public void testStreamFilteredCustomersByRegionAndContainsEmail() throws IOException {
        // Set up filtering criteria
        String firstName = null;
        String lastName = null;
        String email = "@test.com";
        String region = "RegionB";
        LocalDate registrationDateStart = LocalDate.now().minusDays(30);
        LocalDate registrationDateEnd = LocalDate.now();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Stream filtered customers
        customerService.streamFilteredCustomers(firstName, lastName, email, region, registrationDateStart, registrationDateEnd, "id", "asc" , outputStream);


        String output = outputStream.toString();
        // Assert that filtered customers are correctly written to the output stream
        assertThat(output).doesNotContain("\"region\":\"RegionA\"");
    }

    @Test
    @Transactional
    public void testStreamFilteredCustomersByRegionAndContainsEmail_shouldCount50() throws IOException {
        // Set up filtering criteria
        String firstName = null;
        String lastName = null;
        String email = "@test.com";
        String region = "RegionB";
        LocalDate registrationDateStart = LocalDate.now().minusDays(30);
        LocalDate registrationDateEnd = LocalDate.now();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Stream filtered customers
        customerService.streamFilteredCustomers(firstName, lastName, email, region, registrationDateStart, registrationDateEnd, "id", "asc" , outputStream);

        String output = outputStream.toString();

        // Process each line separately
        List<GetCustomerResponse> customerList = new ArrayList<>();
        try (Stream<String> lines = output.lines()) {
            customerList = lines.map(line -> {
                try {
                    return objectMapper.readValue(line, GetCustomerResponse.class);
                } catch (IOException e) {
                    throw new RuntimeException("Error parsing JSON line", e);
                }
            }).collect(Collectors.toList());
        }

        // Assert that the count of items is 50
        assertThat(customerList).hasSize(50);
        // Assert that the output does not contain customers with region "RegionA"
        assertThat(customerList).noneMatch(customer -> "RegionA".equals(customer.region()));
    }
}
