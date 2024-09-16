package com.nihat.jekirdekcase.repositories;

import com.nihat.jekirdekcase.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String name);
}
