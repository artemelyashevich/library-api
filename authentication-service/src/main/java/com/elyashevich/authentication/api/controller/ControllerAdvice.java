package com.elyashevich.authentication.api.controller;

import com.elyashevich.authentication.api.dto.ExceptionBody;
import com.elyashevich.authentication.exception.InvalidTokenException;
import com.elyashevich.authentication.exception.ResourceAlreadyExistsException;
import com.elyashevich.authentication.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ControllerAdvice {

    private static final String NOT_FOUND_MESSAGE = "Not found.";
    private static final String FAILED_VALIDATION_MESSAGE = "Validation failed.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Something went wrong.";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid token";
    private static final String DUPLICATE_ERROR_MESSAGE = "Duplicate error";
    private static final String RESOURCE_ALREADY_EXISTS_MESSAGE = "Resource already exists.";

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody notFound(final ResourceNotFoundException exception) {
        var message = exception.getMessage() == null ? NOT_FOUND_MESSAGE : exception.getMessage();
        log.warn("Resource was not found: '{}'.", message);

        return new ExceptionBody(message);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody token(final InvalidTokenException exception) {
        var message = exception.getMessage() == null ? INVALID_TOKEN_MESSAGE : exception.getMessage();
        log.warn("Token exception: '{}'.", message);

        return new ExceptionBody(message);
    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody sql(final SQLIntegrityConstraintViolationException exception) {
        var message = exception.getMessage() == null ? DUPLICATE_ERROR_MESSAGE : exception.getMessage();
        log.warn("Duplicate: '{}'.", message);

        return new ExceptionBody(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody validation(final MethodArgumentNotValidException exception) {
        var errors = getValidationErrors(exception.getBindingResult());
        return new ExceptionBody(FAILED_VALIDATION_MESSAGE, errors);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody exists(final ResourceAlreadyExistsException exception) {
        var message = exception.getMessage() == null ? RESOURCE_ALREADY_EXISTS_MESSAGE : exception.getMessage();
        log.warn("Already exists: '{}'.", message);

        return new ExceptionBody(message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody exception(final RuntimeException exception) {
        log.error(exception.getMessage(), exception.getCause());
        return new ExceptionBody(UNEXPECTED_ERROR_MESSAGE);
    }


    /**
     * Retrieves validation errors from a BindingResult and converts them into a Map of field names and error messages.
     * <p>
     * The {@code @SuppressWarnings("all")} annotation is used here to suppress all warnings that might be raised by the compiler or other tools
     * for this specific method. It is particularly used to suppress warnings related to unchecked operations when using the {@code toMap} collector
     * in the Stream processing.
     *
     * @param bindingResult the BindingResult containing the field errors
     * @return a Map representing the field names and their corresponding error messages
     */
    @SuppressWarnings("all")
    private static Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                fieldError -> fieldError.getDefaultMessage(),
                                (exist, newMessage) -> exist + " " + newMessage
                        )
                );
    }
}
