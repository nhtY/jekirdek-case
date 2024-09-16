package com.nihat.jekirdekcase.controllers;

import com.nihat.jekirdekcase.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<List<String>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }
}
