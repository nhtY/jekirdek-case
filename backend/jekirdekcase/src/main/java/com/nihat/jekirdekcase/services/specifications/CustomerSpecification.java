package com.nihat.jekirdekcase.services.specifications;

import com.nihat.jekirdekcase.entities.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CustomerSpecification implements Specification<Customer> {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String region;
    private final LocalDate registrationDateStart;
    private final LocalDate registrationDateEnd;

    public CustomerSpecification(String firstName, String lastName, String email, String region,
                                 LocalDate registrationDateStart, LocalDate registrationDateEnd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.region = region;
        this.registrationDateStart = registrationDateStart;
        this.registrationDateEnd = registrationDateEnd;
    }

    @Override
    public Predicate  toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (region != null && !region.isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("region")), region.toLowerCase()));
        }
        if (registrationDateStart != null) {
            LocalDateTime startOfDay = registrationDateStart.atStartOfDay();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), startOfDay));
        }
        if (registrationDateEnd != null) {
            LocalDateTime endOfDay = registrationDateEnd.atTime(LocalTime.MAX);
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("registrationDate"), endOfDay));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
