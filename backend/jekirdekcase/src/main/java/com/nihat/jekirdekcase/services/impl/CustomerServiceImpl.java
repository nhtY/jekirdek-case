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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
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
     * This method is used to stream the filtered customers via SSE.
     * @param firstName
     * @param lastName
     * @param email
     * @param region
     * @param registrationDateStart
     * @param registrationDateEnd
     * @param sortBy
     * @param sortOrder
     * @return SseEmitter object to stream the filtered customers.
     */
    @Override
    @Transactional(readOnly = true)
    public SseEmitter streamFilteredCustomers(String firstName, String lastName, String email, String region,
                                              LocalDate registrationDateStart, LocalDate registrationDateEnd,
                                              String sortBy, String sortOrder) {
        SseEmitter emitter = new SseEmitter();
        try (Stream<Customer> customers = customerRepository.streamAll()) {
            log.info("Streaming filtered customers via SSE");

            // Define comparator based on sorting parameters
            Comparator<Customer> comparator = getComparator(sortBy, sortOrder);

            customers.peek(entityManager::detach)
                    .filter(customer -> matches(firstName, lastName, email, region, customer) || filterDate(registrationDateStart, registrationDateEnd, customer))
                    .sorted(comparator) // Apply sorting
                    .forEach(customer -> {
                        GetCustomerResponse customerResponse = customerMapper.mapToGetCustomerResponse(customer);
                        try {
                            log.info("Sending customer to SSE stream: {}", customerResponse);
                            emitter.send(customerResponse);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            throw new RuntimeException("Error writing customer to SSE stream", e);
                        }
                    });
            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
            throw new RuntimeException("Error filtering customers", e);
        }
        return emitter;
    }

    /**
     * This method is used to make DB handle filtering operations instead of using streams and filtering in memory.
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
    public Page<GetCustomerResponse> getFilteredCustomers(String firstName, String lastName, String email, String region, LocalDate registrationDateStart, LocalDate registrationDateEnd, Pageable pageable) {
        // Create the Specification
        CustomerSpecification spec = new CustomerSpecification(firstName, lastName, email, region, registrationDateStart, registrationDateEnd);

        // Fetch the filtered results
        Page<GetCustomerResponse> response = customerRepository.findAll(spec, pageable).map(customerMapper::mapToGetCustomerResponse);
        return response;
    }

    /**
     * This method is used to stream the filtered customers via OutputStream.
     * @param firstName
     * @param lastName
     * @param email
     * @param region
     * @param registrationDateStart
     * @param registrationDateEnd
     * @param sortBy
     * @param sortOrder
     * @param outputStream
     */
    @Deprecated // This method is not used in the application, because it does not provide a way to stream the data to the client.
    @Override
    @Transactional(readOnly = true)
    public void streamFilteredCustomers(String firstName, String lastName, String email, String region,
                                        LocalDate registrationDateStart, LocalDate registrationDateEnd,
                                        String sortBy, String sortOrder, OutputStream outputStream) {
        try (Stream<Customer> customers = customerRepository.streamAll()) {
            log.info("Streaming filtered customers");

            // Define comparator based on sorting parameters
            Comparator<Customer> comparator = getComparator(sortBy, sortOrder);

            customers.peek(entityManager::detach)
                    .filter(customer -> {
                        log.info("Filtering customer {}", customer.getFirstName());
                        // filter the customers
                        return matches(firstName, lastName, email, region, customer) || filterDate(registrationDateStart, registrationDateEnd, customer);
                    })
                    .sorted(comparator) // Apply sorting
                    .forEach(customer -> {
                        log.info("Mapping customer {}", customer.getFirstName());
                        GetCustomerResponse customerResponse = customerMapper.mapToGetCustomerResponse(customer);
                        try {
                            log.info("Writing customer to output stream {}", customerResponse);
                            // Write each filtered customer to the output stream as JSON
                            objectMapper.writeValue(outputStream, customerResponse);
                            outputStream.write("\n".getBytes()); // Add a newline to separate JSON objects (new-line separated json)
                            outputStream.flush();
                        } catch (Exception e) {
                            throw new RuntimeException("Error writing customer to output stream", e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Error filtering customers", e);
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
     *
     * @param sortBy
     * @param sortOrder
     * @return a Comparator object. By default, it compares based on the id of the Customer.
     */
    private Comparator<Customer> getComparator(String sortBy, String sortOrder) {
        Comparator<Customer> comparator = Comparator.comparing(Customer::getId);

        if ("firstName".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Customer::getFirstName);
        } else if ("lastName".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Customer::getLastName);
        } else if ("email".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Customer::getEmail);
        } else if ("registrationDate".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Customer::getRegistrationDate);
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

}
