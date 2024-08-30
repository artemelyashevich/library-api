package com.elyashevich.authentication.exception;

/**
 * Exception to be thrown when a requested resource is already exists.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
