package org.factory.dst.api.handler;

import org.factory.dst.api.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler for unexpected errors.
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 4)
@ControllerAdvice
public class UnhandledExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Handle unexpected errors.
     *
     * @param exception {@link Throwable}
     * @return {@link ResponseEntity} with information from {@link Throwable}.
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDto> handleThrowable(final Throwable exception) {
        log.error("Unmapped exception occurred.", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.generalError("Internal server error."));
    }
}
