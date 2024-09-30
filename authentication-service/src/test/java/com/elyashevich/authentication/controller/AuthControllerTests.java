package com.elyashevich.authentication.controller;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for testing the register method.
     *
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @MethodSource("provideRequestAndResponse")
    void register(final AuthRequest request, final AuthResponse response) throws Exception {
        when(this.authService.register(request)).thenReturn(response);
        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(response.token()));
        verify(this.authService, times(1)).register(request);
    }

    /**
     * Test case for testing the login method.
     *
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @MethodSource("provideRequestAndResponse")
    void login(final AuthRequest request, final AuthResponse response) throws Exception {
        when(this.authService.login(request)).thenReturn(response);
        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(response.token()));
        verify(this.authService, times(1)).login(request);
    }

    /**
     * Test case for testing the validate method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    void validate() throws Exception {
        var token = "header.payload.signature";
        when(this.authService.validate(token)).thenReturn(token);
        this.mockMvc.perform(post("/api/v1/auth/validate/{token}", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private static Stream<Arguments> provideRequestAndResponse() {
        var request = new AuthRequest(
                "example@example.com",
                "123456789"
        );
        var response = new AuthResponse(
                "header.payload.signature"
        );
        return Stream.of(
                Arguments.of(request, response)
        );
    }
}