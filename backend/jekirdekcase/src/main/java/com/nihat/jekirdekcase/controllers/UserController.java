package com.nihat.jekirdekcase.controllers;

import com.nihat.jekirdekcase.controllers.docs.UserControllerDocs;
import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @Override
    public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.saveUser(createUserRequest));
    }

    @Override
    public ResponseEntity<UpdateUserResponse> updateUser(UpdateUserRequest updateUserRequest, Long id) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest, id));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetUserResponse> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Override
    public ResponseEntity<List<GetUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<Page<GetUserResponse>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}
