package com.elyashevich.authentication.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AuthRequest(
        @Email(message = "Invalid email format.")
        @NotNull(message = "Email must be not null.")
        String email,

        @NotNull(message = "Email must be not null.")
        @Length(
                min = 8,
                message = "Min length of password must be {min}."
        )
        String password
) {
}
