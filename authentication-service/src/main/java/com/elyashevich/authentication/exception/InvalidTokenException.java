package com.elyashevich.authentication.exception;

/**
 * Exception to be thrown when a requested resource is not valid.
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
