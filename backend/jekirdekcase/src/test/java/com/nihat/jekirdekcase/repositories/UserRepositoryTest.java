package com.nihat.jekirdekcase.repositories;

import com.nihat.jekirdekcase.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use the actual database
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        user2 = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .username("janedoe")
                .email("jane.doe@example.com")
                .password("password456")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void testFindAllUsers() {
        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Test
    void testFindUserByUsername() {
        Optional<User> user = userRepository.findByUsername("johndoe");
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testFindUserByEmail() {
        Optional<User> user = userRepository.findByEmail("jane.doe@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("janedoe");
    }

    @Test
    void testFindUserByUsernameOrEmail() {
        Optional<User> user = userRepository.findByUsernameOrEmail("janedoe", "jane.doe@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    public void testUniqueUsername() {
        // Attempt to create a new user with the same username as user1
        User userWithDuplicateUsername = User.builder()
                .firstName("Duplicate")
                .lastName("User")
                .username("johndoe") // Same username as user1
                .email("differentemail@example.com")
                .password("password")
                .build();

        // Assert that saving this user throws a DataIntegrityViolationException
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(userWithDuplicateUsername);
        });
    }

    @Test
    public void testUniqueEmail() {
        // Attempt to create a new user with the same email as user2
        User userWithDuplicateEmail = User.builder()
                .firstName("Duplicate")
                .lastName("User")
                .username("differentusername") // Different username
                .email("jane.doe@example.com") // Same email as user2
                .password("password")
                .build();

        // Assert that saving this user throws a DataIntegrityViolationException
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(userWithDuplicateEmail);
        });

        System.out.println(ex.getMessage()); // could not execute statement [Duplicate entry 'jane.doe@example.com' for key 'user.UKob8kqyqqgmefl0aco34akdtpe'] [insert into user (created_at,email, ....
    }

    @Test
    void testExistsByUsername() {
        Boolean exists = userRepository.existsByUsername("johndoe");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail() {
        Boolean exists = userRepository.existsByEmail("john.doe@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void testCreateUser() {
        User newUser = User.builder()
                .firstName("Alice")
                .lastName("Wonderland")
                .username("alicew")
                .email("alice@example.com")
                .password("password789")
                .build();

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("alicew");
    }

    @Test
    void testUpdateUser() {
        User existingUser = userRepository.findByUsername("johndoe").get();
        existingUser.setLastName("Smith");

        User updatedUser = userRepository.save(existingUser);

        assertThat(updatedUser.getLastName()).isEqualTo("Smith");
    }

    @Test
    void testDeleteUser() {
        userRepository.deleteById(user1.getId());

        Optional<User> deletedUser = userRepository.findById(user1.getId());
        assertThat(deletedUser).isNotPresent();
    }
}