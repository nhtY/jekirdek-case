package com.nihat.jekirdekcase.services.impl;


import com.nihat.jekirdekcase.auth.CustomUserDetails;
import com.nihat.jekirdekcase.auth.CustomUserDetailsService;
import com.nihat.jekirdekcase.services.AuthenticationService;
import com.nihat.jekirdekcase.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;


    public String login(String email, String password, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authenticationToken);

        CustomUserDetails userDetails =  userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);

        // Set the token in a cookie for browser clients
        response.addCookie(jwtUtil.createCookie(token, jwtUtil.getCookieName(), jwtUtil.getCookieMaxAge()));

        return token;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the cookie for browser clients
        Cookie cookie = new Cookie(jwtUtil.getCookieName(), null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public UserDetails getCurrentUser(String token) {
        return userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
    }
}

