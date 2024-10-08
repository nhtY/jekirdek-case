package com.nihat.jekirdekcase.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nihat.jekirdekcase.auth.CustomUserDetails;
import com.nihat.jekirdekcase.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), HashMap.class);
            String email = credentials.get("email");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        // Determine client type
        String deviceOs = request.getHeader("device-os");
        if (deviceOs != null && deviceOs.contains("Browser")) {
            // For browser clients, set the token in a cookie
            response.addCookie(jwtUtil.createCookie(token, jwtUtil.getCookieName(), jwtUtil.getCookieMaxAge()));
        } else {
            // For non-browser clients, set the token in the Authorization header
            response.setHeader("Authorization", "Bearer " + token);
        }

        chain.doFilter(request, response);
    }
}
