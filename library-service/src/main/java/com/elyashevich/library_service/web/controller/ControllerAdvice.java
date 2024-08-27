package com.elyashevich.library_service.web.controller;

import com.elyashevich.library_service.domain.exception.NotFoundException;
import com.elyashevich.library_service.web.dto.ExceptionBody;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionBody> notFound(
            final NotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ExceptionBody(
                                exception.getMessage() == null
                                        ? "Not found."
                                        : exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> validation(
            final MethodArgumentNotValidException exception
    ) {
        var errors = this.getValidationErrors(exception.getBindingResult());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ExceptionBody("Validation failed", errors)
                );
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> exception(
            final Exception e
    ) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ExceptionBody("Resource already exists")
                );
    }

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                DefaultMessageSourceResolvable::getDefaultMessage,
                                (exist, newMessage) -> exist + " " + newMessage
                        )
                );
    }
}