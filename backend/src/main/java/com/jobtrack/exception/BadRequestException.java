package com.jobtrack.exception;

/**
 * Exception thrown when client request parameters or business constraints fail (e.g. duplicate username or email).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
