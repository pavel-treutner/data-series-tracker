package org.factory.dst.api.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.factory.dst.api.dto.ErrorDto;
import org.factory.dst.api.dto.ErrorItemDto;
import org.factory.dst.process.exception.NotFoundException;
import org.factory.dst.process.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception handler for expected errors, i.e. thrown by business logic.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class HandledExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Handle custom validation errors.
     *
     * @param exception {@link ValidationException}
     * @return {@link ResponseEntity} with information from {@link ValidationException}.
     */
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorDto> handleValidationException(final ValidationException exception) {
        logException(exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.validationError(exception.getMessage()));
    }

    /**
     * Handle missing resources.
     *
     * @param exception {@link NotFoundException}
     * @return {@link ResponseEntity} with filled information from {@link NotFoundException}.
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(final NotFoundException exception) {
        logException(exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDto.notFoundError(exception.getMessage()));
    }

    /**
     * Handle Jackson errors.
     *
     * @param exception {@link JsonProcessingException}
     * @return {@link ResponseEntity} with information from {@link JsonProcessingException}.
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorDto> handleJsonProcessingException(final JsonProcessingException exception) {
        logException(exception);

        ErrorItemDto errorItem;
        if (exception instanceof JsonMappingException) {
            JsonMappingException mappingException = (JsonMappingException) exception;
            errorItem = new ErrorItemDto("FIELD_INVALID_FORMAT",
                    null, getFieldPath(mappingException));
        } else {
            String subject = "Error at [line: " + exception.getLocation().getLineNr() + ", column: "
                    + exception.getLocation().getColumnNr() + "]";
            errorItem = new ErrorItemDto("INVALID_JSON", subject, null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.validationError(exception.getMessage(),
                Collections.singletonList(errorItem)));
    }

    private String getFieldPath(JsonMappingException exception) {
        List<JsonMappingException.Reference> path = exception.getPath();
        if (path == null || path.isEmpty()) {
            return null;
        }
        return path.stream()
                .map(p -> p.getFieldName() == null ? String.valueOf(p.getIndex()) : p.getFieldName())
                .collect(Collectors.joining("."));
    }

    private void logException(final Exception exception) {
        log.info("Handling {}: {}", exception.getClass().getName(), exception.getMessage());
    }
}
