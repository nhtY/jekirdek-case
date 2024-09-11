package com.nihat.jekirdekcase.repositories;

import com.nihat.jekirdekcase.entities.Customer;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.stream.Stream;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c")
    @QueryHints(
            @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "10")
    )
    Stream<Customer> streamAll();
}
