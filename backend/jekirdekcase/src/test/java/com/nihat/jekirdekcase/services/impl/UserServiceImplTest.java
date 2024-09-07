package com.nihat.jekirdekcase.services.impl;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.entities.User;
import com.nihat.jekirdekcase.exceptions.AlreadyExistsException;
import com.nihat.jekirdekcase.exceptions.UserNotFoundException;
import com.nihat.jekirdekcase.mappers.UserMapper;
import com.nihat.jekirdekcase.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;
    private CreateUserResponse createUserResponse;
    private UpdateUserResponse updateUserResponse;
    private GetUserResponse getUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");

        createUserRequest = new CreateUserRequest("John", "Doe", "johndoe", "john.doe@example.com", "password");
        updateUserRequest = new UpdateUserRequest("John", "Doe", "john.doe@example.com", "johndoe");
        createUserResponse = new CreateUserResponse(1L, "John", "Doe", "johndoe", "john.doe@example.com", "2023-09-01");
        updateUserResponse = new UpdateUserResponse(1L, "John", "Doe", "johndoe", "john.doe@example.com", null);
        getUserResponse = new GetUserResponse(1L, "John", "Doe", "johndoe", "john.doe@example.com", "2023-09-01", "2023-09-01");
    }

    @Test
    void saveUser_ShouldReturnCreateUserResponse() {
        when(userMapper.mapToUser(createUserRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToCreateUserResponse(user)).thenReturn(createUserResponse);

        CreateUserResponse response = userService.saveUser(createUserRequest);

        assertThat(response).isEqualTo(createUserResponse);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_ShouldThrowAlreadyExistsExceptionIfUsernameOrEmailExists() {
        when(userMapper.mapToUser(createUserRequest)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> userService.saveUser(createUserRequest));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateUser_ShouldReturnUpdateUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapToUser(updateUserRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToUpdateUserResponse(user)).thenReturn(updateUserResponse);

        UpdateUserResponse response = userService.updateUser(updateUserRequest, 1L);

        assertThat(response).isEqualTo(updateUserResponse);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUser_ShouldReturnGetUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapToGetUserResponse(user)).thenReturn(getUserResponse);

        GetUserResponse response = userService.getUser(1L);

        assertThat(response).isEqualTo(getUserResponse);
    }

    @Test
    void getAllUsers_ShouldReturnListOfGetUserResponses() {
        List<User> users = List.of(user);
        List<GetUserResponse> userResponses = List.of(getUserResponse);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.mapToGetUserResponseList(users)).thenReturn(userResponses);

        List<GetUserResponse> responses = userService.getAllUsers();

        assertThat(responses).isEqualTo(userResponses);
    }

    @Test
    void getAllUsersWithPagination_ShouldReturnPageOfGetUserResponses() {
        List<User> users = List.of(user);
        Page<User> userPage = new PageImpl<>(users);
        List<GetUserResponse> userResponses = List.of(getUserResponse);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.mapToGetUserResponseList(users)).thenReturn(userResponses);

        Page<GetUserResponse> responsePage = userService.getAllUsers(Pageable.unpaged());

        assertThat(responsePage.getContent()).isEqualTo(userResponses);
    }
}
