package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public CreateUserResponse saveUser(CreateUserRequest createUserRequest) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest, Long id) {
        return null;
    }

    @Override
    public GetUserResponse getUser(Long id) {
        return null;
    }

    @Override
    public List<GetUserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public Page<GetUserResponse> getAllUsers(Pageable pageable) {
        return null;
    }
}
