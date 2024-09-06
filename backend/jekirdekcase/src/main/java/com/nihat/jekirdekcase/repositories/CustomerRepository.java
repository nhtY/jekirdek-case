package com.nihat.jekirdekcase.repositories;

import com.nihat.jekirdekcase.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
