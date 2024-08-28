package com.elyashevich.library_service.api.dto;

import com.elyashevich.library_service.api.validation.OnCreate;
import com.elyashevich.library_service.api.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDto(

        UUID id,

        @NotNull(message = "Book's id must be not null.", groups = {OnCreate.class, OnUpdate.class})
        UUID bookId,

        @NotNull(message = "Expire time id must be not null.", groups = {OnCreate.class, OnUpdate.class})
        LocalDateTime expireIn,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
