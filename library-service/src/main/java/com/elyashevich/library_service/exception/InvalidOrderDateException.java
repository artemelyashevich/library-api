package com.elyashevich.library_service.exception;

/**
 * Exception to be thrown when a requested resource has not valid date.
 */
public class InvalidOrderDateException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidOrderDateException(final String message) {
        super(message);
    }
}
