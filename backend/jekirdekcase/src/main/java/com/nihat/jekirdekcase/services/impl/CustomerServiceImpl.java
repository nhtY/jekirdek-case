package com.nihat.jekirdekcase.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nihat.jekirdekcase.dtos.requests.CreateCustomerRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateCustomerRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.GetCustomerResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateCustomerResponse;
import com.nihat.jekirdekcase.entities.Customer;
import com.nihat.jekirdekcase.exceptions.AlreadyExistsException;
import com.nihat.jekirdekcase.exceptions.ResourceNotFoundException;
import com.nihat.jekirdekcase.logging.Loggable;
import com.nihat.jekirdekcase.mappers.CustomerMapper;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import com.nihat.jekirdekcase.services.CustomerService;
import com.nihat.jekirdekcase.services.specifications.CustomerSpecification;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Loggable
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;
    private final CustomerMapper customerMapper;
    private final ObjectMapper objectMapper; // to write data on http response, for the streaming part.

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
        return customerRepository.findById(id)
                .map(customer -> {
                    if (!customer.getEmail().equals(updateCustomerRequest.email()) && customerRepository.existsByEmail(updateCustomerRequest.email())) {
                        throw new AlreadyExistsException("Customer with this email already exists.");
                    }
                    customer.setFirstName(updateCustomerRequest.firstName());
                    customer.setLastName(updateCustomerRequest.lastName());
                    customer.setEmail(updateCustomerRequest.email());
                    customer.setRegion(updateCustomerRequest.region());
                    return customerMapper.mapToUpdateCustomerResponse(customerRepository.save(customer));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found."));
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

    /**
     * This method makes DB handle filtering operations instead of using streams and filtering in memory.
     * @param firstName
     * @param lastName
     * @param email
     * @param region
     * @param registrationDateStart
     * @param registrationDateEnd
     * @param pageable
     * @return Page<GetCustomerResponse> object containing the filtered customers.
     */
    @Override
    public Page<GetCustomerResponse> filterCustomersUsingSpecs(String firstName, String lastName, String email, String region, LocalDate registrationDateStart, LocalDate registrationDateEnd, Pageable pageable) {
        // Create the Specification
        CustomerSpecification spec = new CustomerSpecification(firstName, lastName, email, region, registrationDateStart, registrationDateEnd);

        // Fetch the filtered results
        Page<GetCustomerResponse> response = customerRepository.findAll(spec, pageable).map(customerMapper::mapToGetCustomerResponse);
        return response;
    }


    /**
     * This method handles filtering using streams. The operation is done on memory instead of the database.
     * @param firstName
     * @param lastName
     * @param email
     * @param region
     * @param registrationDateStart
     * @param registrationDateEnd
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetCustomerResponse> filterCustomersUsingStream(String firstName, String lastName, String email, String region, LocalDate registrationDateStart, LocalDate registrationDateEnd, Pageable pageable) {
        // Initialize AtomicLong to count the total number of filtered customers
        AtomicLong totalFilteredCount = new AtomicLong(0);

        // Open the stream from the repository
        try (Stream<Customer> customers = customerRepository.streamAll()) {

            // Build comparator using multiple sort fields
            Comparator<Customer> comparator = buildComparator(pageable.getSort());

            // Stream filtering, sorting, and counting in one pass
            List<GetCustomerResponse> paginatedCustomers = customers.peek(entityManager::detach)
                    .filter(customer -> {
                        boolean matches = matches(firstName, lastName, email, region, customer)
                                && filterDate(registrationDateStart, registrationDateEnd, customer);
                        if (matches) {
                            totalFilteredCount.incrementAndGet(); // Increment the counter if the customer matches
                        }
                        return matches;
                    })
                    .sorted(comparator)  // Apply sorting
                    .skip(pageable.getOffset())   // Apply pagination: skip to the correct page
                    .limit(pageable.getPageSize())  // Limit to the page size
                    .map(customerMapper::mapToGetCustomerResponse)
                    .toList();

            // Create and return a Page object with the filtered data and total count
            return new PageImpl<>(paginatedCustomers, pageable, totalFilteredCount.get());
        }
    }


    /**
     * Filter the customers based on the given parameters
     * @param firstName
     * @param lastName
     * @param email
     * @param region
     * @param customer the customer to check, came from the database stream
     * @return true if the customer matches the filter, false otherwise
     */
    private boolean matches(String firstName, String lastName, String email, String region, Customer customer) {
        // Filter for firstName containing
        boolean matches = (isNullOrEmptyOrBlank(firstName) || customer.getFirstName().toLowerCase().contains(firstName.toLowerCase()));
        // Filter for lastName containing
        matches = matches && (isNullOrEmptyOrBlank(lastName) || customer.getLastName().toLowerCase().contains(lastName.toLowerCase()));
        // Filter for email containing
        matches = matches && (isNullOrEmptyOrBlank(email) || customer.getEmail().toLowerCase().contains(email.toLowerCase()));
        // Filter for region equals
        matches = matches && (isNullOrEmptyOrBlank(region) || customer.getRegion().toLowerCase().equals(region.toUpperCase()));

        return matches;
    }

    /**
     *
     * @param registrationDateStart
     * @param registrationDateEnd
     * @param customer - customer from DB to filter
     * @return true if the customer passes the filter.
     */
    private boolean filterDate (LocalDate registrationDateStart, LocalDate registrationDateEnd, Customer customer) {

        // Filter for registrationDate between
        if (registrationDateStart != null && registrationDateEnd != null) {
            LocalDateTime startOfDay = registrationDateStart.atStartOfDay();
            LocalDateTime endOfDay = registrationDateEnd.atTime(LocalTime.MAX);
            return (customer.getRegistrationDate().isAfter(startOfDay) || customer.getRegistrationDate().isEqual(startOfDay)) && (customer.getRegistrationDate().isBefore(endOfDay) || customer.getRegistrationDate().isEqual(endOfDay));
        } else if (registrationDateStart != null) {
            LocalDateTime startOfDay = registrationDateStart.atStartOfDay();
            LocalDateTime endOfDay = registrationDateStart.atTime(LocalTime.MAX);
            return (customer.getRegistrationDate().isAfter(startOfDay) || customer.getRegistrationDate().isEqual(startOfDay)) && (customer.getRegistrationDate().isBefore(endOfDay) || customer.getRegistrationDate().isEqual(endOfDay));
        }

        return false;
    }
    
    private boolean isNullOrEmptyOrBlank(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    /**
     * Builds a comparator based on multiple sorting fields.
     * @param sort the Sort object containing multiple sorting orders
     * @return a chained Comparator that applies sorting based on the specified fields and directions
     */
    private Comparator<Customer> buildComparator(Sort sort) {
        Comparator<Customer> comparator = Comparator.comparing(Customer::getId); // Default comparator

        // Iterate over each Sort.Order and build the comparator
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            boolean ascending = order.isAscending();

            // Build comparator based on the field to be sorted
            Comparator<Customer> fieldComparator;
            if ("firstName".equalsIgnoreCase(property)) {
                fieldComparator = Comparator.comparing(Customer::getFirstName, String.CASE_INSENSITIVE_ORDER);
            } else if ("lastName".equalsIgnoreCase(property)) {
                fieldComparator = Comparator.comparing(Customer::getLastName, String.CASE_INSENSITIVE_ORDER);
            } else if ("email".equalsIgnoreCase(property)) {
                fieldComparator = Comparator.comparing(Customer::getEmail, String.CASE_INSENSITIVE_ORDER);
            } else if ("registrationDate".equalsIgnoreCase(property)) {
                fieldComparator = Comparator.comparing(Customer::getRegistrationDate);
            } else {
                fieldComparator = Comparator.comparing(Customer::getId);  // Default to sorting by ID
            }

            // Apply ascending/descending order
            if (!ascending) {
                fieldComparator = fieldComparator.reversed();
            }

            // Chain comparators using thenComparing
            comparator = comparator.thenComparing(fieldComparator);
        }

        return comparator;
    }

}
