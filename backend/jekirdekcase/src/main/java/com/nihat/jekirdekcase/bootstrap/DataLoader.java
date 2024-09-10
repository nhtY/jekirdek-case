package com.nihat.jekirdekcase.bootstrap;


import com.nihat.jekirdekcase.entities.Customer;
import com.nihat.jekirdekcase.logging.Loggable;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


@Component
@Slf4j
@Loggable
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    public DataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
       insertOneThousandCustomers();
    }

    private void insertOneThousandCustomers() {

        if (customerRepository.count() == 0) {
            log.info("Inserting 2 thousand customers...");
            // Define lists of names, surnames, regions, and email domains
            List<String> firstNames = List.of("Ahmet", "Mehmet", "Ayşe", "Fatma", "Ali", "Mustafa", "Emine", "Hüseyin", "Zeynep", "Yusuf");
            List<String> lastNames = List.of("Yılmaz", "Kaya", "Demir", "Şahin", "Çelik", "Öztürk", "Acar", "Koç", "Arslan", "Korkmaz");
            List<String> regions = List.of("A", "B", "C", "D");
            List<String> emailDomains = List.of("@gmail.com", "@asd.com", "@aa.com");

            List<Customer> customers = new ArrayList<>();
            Random random = new Random();

            // Create 1 thousand customers
            for (int i = 0; i < 2_000; i++) {
                String firstName = firstNames.get(random.nextInt(firstNames.size()));
                String lastName = lastNames.get(random.nextInt(lastNames.size()));
                String region = regions.get(random.nextInt(regions.size()));
                String emailDomain = emailDomains.get(random.nextInt(emailDomains.size()));
                String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + emailDomain;  // Ensure unique email

                // Build the customer
                Customer customer = Customer.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .region(region)
                        .build();

                customers.add(customer);

                // Batch insert every 100 records to avoid memory issues
                if (customers.size() == 100) {
                    customerRepository.saveAll(customers);
                    customers.clear();  // Clear the list for the next batch
                }
            }

            // Save any remaining customers
            if (!customers.isEmpty()) {
                customerRepository.saveAll(customers);
            }

            System.out.println("Inserted 1 thousand customers.");
        }
        log.info("Some customers already exist in the database.");
    }
}
