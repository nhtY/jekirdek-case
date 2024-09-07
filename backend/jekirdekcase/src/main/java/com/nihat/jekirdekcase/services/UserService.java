package com.nihat.jekirdekcase.services;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    CreateUserResponse saveUser(CreateUserRequest createUserRequest);
    void deleteUser(Long id);
    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest, Long id);
    GetUserResponse getUser(Long id);
    List<GetUserResponse> getAllUsers();
    Page<GetUserResponse> getAllUsers(Pageable pageable);
}
