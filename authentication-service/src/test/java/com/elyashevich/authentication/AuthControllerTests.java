package com.elyashevich.authentication;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test class for testing the AuthController.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

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
    @Test
    public void register() throws Exception {
        var authRequest = getAuthRequestExample();
        var authResponse = getAuthResponseExample();
        when(this.authService.register(authRequest)).thenReturn(authResponse);
        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(authResponse.token()));
        verify(this.authService, times(1)).register(authRequest);
    }

    /**
     * Test case for testing the login method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void login() throws Exception {
        var authRequest = getAuthRequestExample();
        var authResponse = getAuthResponseExample();
        when(this.authService.login(authRequest)).thenReturn(authResponse);
        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(authResponse.token()));
        verify(this.authService, times(1)).login(authRequest);
    }

    /**
     * Test case for testing the validate method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void validate() throws Exception {
        var token = "header.payload.signature";
        when(this.authService.validate(token)).thenReturn(token);
        this.mockMvc.perform(post("/api/v1/auth/validate/{token}", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private static AuthRequest getAuthRequestExample() {
        return new AuthRequest(
                "example@example.com",
                "123456789"
        );
    }

    private static AuthResponse getAuthResponseExample() {
        return new AuthResponse(
                "header.payload.signature"
        );
    }
}
