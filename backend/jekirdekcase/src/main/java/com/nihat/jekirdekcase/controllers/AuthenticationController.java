package com.nihat.jekirdekcase.controllers;

import com.nihat.jekirdekcase.controllers.docs.AuthControllerDocs;
import com.nihat.jekirdekcase.services.AuthenticationService;
import com.nihat.jekirdekcase.dtos.requests.LoginRequest;
import com.nihat.jekirdekcase.dtos.responses.CurrentUserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController implements AuthControllerDocs {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<String> login(LoginRequest loginRequest, HttpServletResponse response) {
        String token = authenticationService.login(loginRequest.email(), loginRequest.password(), response);
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CurrentUserResponse> getCurrentUser(HttpServletRequest request) {
        // Extract token from cookies
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("authToken")) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = authenticationService.getCurrentUser(token);
        CurrentUserResponse response = new CurrentUserResponse(userDetails.getUsername(), userDetails.getAuthorities().stream().map(Object::toString).toList());
        return ResponseEntity.ok(response);
    }
}
