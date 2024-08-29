package com.elyashevich.authentication.api.dto;

import jakarta.validation.constraints.NotNull;

public record AuthResponse(
        @NotNull(message = "Token must be not null.")
        String token
) {
}
