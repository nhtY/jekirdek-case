package com.nihat.jekirdekcase.services;

import com.nihat.jekirdekcase.dtos.requests.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    String login(String email, String password, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    UserDetails getCurrentUser(String token);
}
