package com.elyashevich.book.api.dto;

import com.elyashevich.book.api.validation.OnCreate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
public class OrderDto {

    @NotNull(message = "Book's id must be not null.", groups = OnCreate.class)
    private UUID bookId;

    private LocalDateTime expireIn;
}
