package com.elyashevich.authentication.controller;

import com.elyashevich.authentication.api.controller.impl.AuthControllerImpl;
import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.config.SecurityConfig;
import com.elyashevich.authentication.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthControllerImpl.class)
@Import(SecurityConfig.class)
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideAuthRequest")
    void registerTest(final AuthRequest request) throws Exception {
        // Arrange
        var response = new AuthResponse("token123");
        when(authService.register(request)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthRequest")
    void loginTest(final AuthRequest request) throws Exception {
        // Arrange
        AuthResponse response = new AuthResponse("token123");
        when(authService.login(request)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token123"));
        verify(authService, times(1)).login(request);
    }

    @Test
    @WithMockUser
    void testValidate() throws Exception {
        // Arrange
        String validToken = "some.token.example";

        when(authService.validate(validToken)).thenReturn(validToken);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/{token}", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(validToken));
        verify(authService, times(1)).validate(validToken);
    }

    private static Stream<Arguments> provideAuthRequest() {
        var request = new AuthRequest("test@example.com", "password123");
        return Stream.of(
                Arguments.of(request)
        );
    }
}