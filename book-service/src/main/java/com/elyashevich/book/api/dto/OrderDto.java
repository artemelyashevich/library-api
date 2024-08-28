package com.elyashevich.book.api.dto;

import com.elyashevich.book.api.validation.OnCreate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDto(

        @NotNull(message = "Book's id must be not null.", groups = OnCreate.class)
        UUID bookId,

        @NotNull(message = "Expire time id must be not null.", groups = OnCreate.class)
        LocalDateTime expireIn

) {
}
