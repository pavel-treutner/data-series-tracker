package org.factory.dst.process.exception;

/**
 * Thrown on missing resources.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
