package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.entities.AppRole;
import com.nihat.jekirdekcase.repositories.AppRoleRepository;
import com.nihat.jekirdekcase.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final AppRoleRepository appRoleRepository;
    @Override
    public List<String> getRoles() {
        return appRoleRepository.findAll().stream()
                .map(AppRole::getRoleName)
                .toList();
    }
}
