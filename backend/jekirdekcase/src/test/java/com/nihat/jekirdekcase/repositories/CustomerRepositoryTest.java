package com.nihat.jekirdekcase.repositories;

import com.nihat.jekirdekcase.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use the actual database
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        customer1 = Customer.builder()
                .firstName("Jack")
                .lastName("Sparrow")
                .email("jack.sparrow@blackpearl.com")
                .region("Caribbean")
                .build();

        customer2 = Customer.builder()
                .firstName("Will")
                .lastName("Turner")
                .email("will.turner@blackpearl.com")
                .region("Port Royal")
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);
    }

    @Test
    void testFindAllCustomers() {
        assertThat(customerRepository.findAll()).hasSize(2);
    }

    @Test
    void testFindCustomerById() {
        Customer customer = customerRepository.findById(customer1.getId()).orElse(null);
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo("Jack");
    }

    @Test
    void testCreateCustomer() {
        Customer newCustomer = Customer.builder()
                .firstName("Elizabeth")
                .lastName("Swann")
                .email("elizabeth.swann@portroyal.com")
                .region("Port Royal")
                .build();

        Customer savedCustomer = customerRepository.save(newCustomer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getEmail()).isEqualTo("elizabeth.swann@portroyal.com");
    }

    @Test
    void testUpdateCustomer() {
        Customer existingCustomer = customerRepository.findById(customer1.getId()).get();
        existingCustomer.setLastName("Blacksmith");

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        assertThat(updatedCustomer.getLastName()).isEqualTo("Blacksmith");
    }

    @Test
    void testDeleteCustomer() {
        customerRepository.deleteById(customer2.getId());

        assertThat(customerRepository.findById(customer2.getId())).isNotPresent();
    }

    // Test for uniqueness of email field
    @Test
    void testUniqueEmailConstraint() {
        // Try to save a new customer with an email that already exists
        Customer customerWithDuplicateEmail = Customer.builder()
                .firstName("Duplicate")
                .lastName("Customer")
                .email("jack.sparrow@blackpearl.com") // Duplicate email
                .region("Caribbean")
                .build();

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.saveAndFlush(customerWithDuplicateEmail);
        });

        // Print the error message to understand the constraint violation
        System.out.println("Error Message: " + exception.getMessage());
    }
}