package org.factory.dst.process.exception;

/**
 * Thrown on validation errors.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
