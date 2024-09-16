package com.nihat.jekirdekcase.controllers.validation;

import com.nihat.jekirdekcase.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void whenLoginWithInvalidEmail_thenServiceMethodNotCalled() throws Exception {
        String request = """
            {
                "email": "",
                "password": ""
            }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .header("device-os", "ios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).login(anyString(), anyString(), any(HttpServletResponse.class));
    }

    @Test
    public void whenLoginWithMissingPassword_thenServiceMethodNotCalled() throws Exception {
        String request = """
            {
                "email": "john@example.com",
                "password": ""
            }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .header("device-os", "Browser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).login(anyString(), anyString(), any(HttpServletResponse.class));
    }
}
