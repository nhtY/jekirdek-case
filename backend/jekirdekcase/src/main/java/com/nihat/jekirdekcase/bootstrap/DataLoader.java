package com.nihat.jekirdekcase.bootstrap;


import com.nihat.jekirdekcase.entities.AppRole;
import com.nihat.jekirdekcase.entities.Customer;
import com.nihat.jekirdekcase.entities.User;
import com.nihat.jekirdekcase.logging.Loggable;
import com.nihat.jekirdekcase.repositories.AppRoleRepository;
import com.nihat.jekirdekcase.repositories.CustomerRepository;
import com.nihat.jekirdekcase.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;


@Component
@Slf4j
@Loggable
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CustomerRepository customerRepository, UserRepository userRepository, AppRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        insertRoles();
        insertUsers();
        insertTwoThousandCustomers();
    }

    protected void insertRoles() {
        log.info("attempting to insert roles...");
        if (roleRepository.count() == 0) {
            log.info("Inserting roles...");
            // Define a list of roles
            List<AppRole> roles = List.of(
                AppRole.builder().roleName("ROLE_USER").build(),
                    AppRole.builder().roleName("ROLE_ADMIN").build()
            );

            roleRepository.saveAll(roles);
            log.info("Inserted roles.");
        }
    }


    private void insertUsers() {
        log.info("attempting to insert users...");
        if (userRepository.count() == 0) {
            insertRoles();
            log.info("Inserting users...");
            // Define a list of users
            List<User> users = List.of(
                User.builder()
                        .firstName("admin")
                        .lastName("admin")
                        .username("admin")
                        .email("admin@email.com")
                        .password(passwordEncoder.encode("admin"))
                        .roles(Set.of(roleRepository.findByRoleName("ROLE_ADMIN")))
                        .build(),
                    User.builder()
                        .firstName("user")
                        .lastName("user")
                        .username("user")
                        .email("user@email.com")
                        .password(passwordEncoder.encode("user"))
                        .roles(Set.of(roleRepository.findByRoleName("ROLE_USER")))
                        .build());

            userRepository.saveAll(users);

        }


    }


    private void insertTwoThousandCustomers() {
        log.info("attempting to insert 2 thousand customers...");
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

            System.out.println("Inserted 2 thousand customers.");
        }
    }
}
