package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.entities.User;
import com.nihat.jekirdekcase.exceptions.AlreadyExistsException;
import com.nihat.jekirdekcase.exceptions.ResourceNotFoundException;
import com.nihat.jekirdekcase.logging.Loggable;
import com.nihat.jekirdekcase.mappers.UserMapper;
import com.nihat.jekirdekcase.repositories.UserRepository;
import com.nihat.jekirdekcase.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Loggable
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public CreateUserResponse saveUser(CreateUserRequest createUserRequest) {
        try {
            return userMapper.mapToCreateUserResponse(userRepository.save(userMapper.mapToUser(createUserRequest)));
        } catch (DataIntegrityViolationException e) {
            throw throwAlreadyExistsExceptionForUserCreation(createUserRequest.username(), createUserRequest.email());
        }
    }

    private AlreadyExistsException throwAlreadyExistsExceptionForUserCreation(String username, String email) {

        boolean usernameExists = userRepository.existsByUsername(username);
        boolean emailExists = userRepository.existsByEmail(email);

        if (usernameExists && emailExists) {
            return new AlreadyExistsException("User with this username and email already exists.");
        } else if (usernameExists) {
            return new AlreadyExistsException("User with this username already exists.");
        } else if (emailExists) {
            return new AlreadyExistsException("User with this email already exists.");
        } else {
            return new AlreadyExistsException("User with this username and email already exists.");
        }

    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest, Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updateUserRequest.firstName());
                    user.setLastName(updateUserRequest.lastName());
                    return userMapper.mapToUpdateUserResponse(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));
    }

    @Override
    public GetUserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));
        return userMapper.mapToGetUserResponse(user);
    }

    @Override
    public List<GetUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.mapToGetUserResponseList(users);
    }

    @Override
    public Page<GetUserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::mapToGetUserResponse);
    }
}
