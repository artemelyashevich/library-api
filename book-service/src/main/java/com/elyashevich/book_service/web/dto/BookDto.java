package com.elyashevich.book_service.web.dto;

import com.elyashevich.book_service.web.validation.OnCreate;
import com.elyashevich.book_service.web.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookDto(
        UUID id,

        @NotNull(message = "Title must be not null.", groups = {OnCreate.class, OnUpdate.class})
        @Length(
                min = 1,
                max = 100,
                message = "Title must be in {min} and {max}.",
                groups = {OnCreate.class, OnUpdate.class}
        )
        String title,

        @NotNull(message = "Description must be not null.", groups = {OnCreate.class, OnUpdate.class})
        @Length(
                min = 1,
                max = 255,
                message = "Description must be in {min} and {max}.",
                groups = {OnCreate.class, OnUpdate.class}
        )
        String description,

        @NotNull(message = "Genre must be not null.", groups = {OnCreate.class, OnUpdate.class})
        @Length(
                min = 1,
                max = 50,
                message = "Genre must be in {min} and {max}.",
                groups = {OnCreate.class, OnUpdate.class}
        )
        String genre,

        @NotNull(message = "Author must be not null.", groups = {OnCreate.class, OnUpdate.class})
        @Length(
                min = 1,
                max = 255,
                message = "Author must be in {min} and {max}.",
                groups = {OnCreate.class, OnUpdate.class}
        )
        String author,

        @NotNull(message = "ISBN must be not null.", groups = OnCreate.class)
        @Pattern(
                regexp = "^\\d{3}-\\d{10}$",
                message = "Invalid ISBN format. Should be in the format XXX-XXXXXXXXXX",
                groups = OnUpdate.class
        )
        String isbn,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
