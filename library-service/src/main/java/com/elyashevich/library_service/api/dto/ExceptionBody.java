package com.elyashevich.library_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {

    private String message;

    private Map<String, String> errors;

    public ExceptionBody(String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }
}
