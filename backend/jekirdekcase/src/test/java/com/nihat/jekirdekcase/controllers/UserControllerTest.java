package com.nihat.jekirdekcase.controllers;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "jdoe", "john.doe@example.com", "password123");
        CreateUserResponse response = new CreateUserResponse(1L, "John", "Doe", "jdoe", "john.doe@example.com", LocalDateTime.now());

        when(userService.saveUser(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"username\":\"jdoe\", \"email\":\"john.doe@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).saveUser(any(CreateUserRequest.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("John", "Doe");
        UpdateUserResponse response = new UpdateUserResponse(1L, "John", "Doe", "jdoe", "john.doe@example.com", LocalDateTime.now());

        when(userService.updateUser(any(UpdateUserRequest.class), anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).updateUser(any(UpdateUserRequest.class), anyLong());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testGetUserById() throws Exception {
        GetUserResponse response = new GetUserResponse(1L, "John", "Doe", "jdoe", "john.doe@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUser(anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).getUser(1L);
    }

    @Test
    void testGetAllUsers() throws Exception {
        GetUserResponse user1 = new GetUserResponse(1L, "John", "Doe", "jdoe", "john.doe@example.com", LocalDateTime.now(), LocalDateTime.now());
        GetUserResponse user2 = new GetUserResponse(2L, "Jane", "Smith", "jsmith", "jane.smith@example.com", LocalDateTime.now(), LocalDateTime.now());
        List<GetUserResponse> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].username").value("jdoe"))
                .andExpect(jsonPath("$[0].email").value(user1.email()));

        verify(userService, times(1)).getAllUsers();
    }


    @Test
    void testGetAllUsersWithPage() throws Exception {
        GetUserResponse user1 = new GetUserResponse(1L, "John", "Doe", "jdoe", "john.doe@example.com", LocalDateTime.now(), LocalDateTime.now());
        GetUserResponse user2 = new GetUserResponse(2L, "Jane", "Smith", "jsmith", "jane.smith@example.com", LocalDateTime.now(), LocalDateTime.now());
        List<GetUserResponse> users = List.of(user1, user2);
        Page<GetUserResponse> userPage = new PageImpl<>(users);

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/page?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[0].username").value("jdoe"))
                .andExpect(jsonPath("$.content[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.content[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.content[1].username").value("jsmith"))
                .andExpect(jsonPath("$.content[1].email").value("jane.smith@example.com"));

        verify(userService, times(1)).getAllUsers(any(Pageable.class));
    }
}