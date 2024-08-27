package com.elyashevich.book_service.web.dto;

import com.elyashevich.book_service.web.validation.OnCreate;
import com.elyashevich.book_service.web.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record OrderDto(

        @NotNull(message = "Book's id must be not null.")
        Integer bookId,

        @NotNull(message = "Expire time id must be not null.")
        LocalDateTime expireIn
) {
}
