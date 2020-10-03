package org.factory.dst.api.handler;

import org.factory.dst.api.dto.ErrorDto;
import org.factory.dst.api.dto.ErrorItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customized Spring exception handler.
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String FIELD_INVALID_VALUE = "FIELD_INVALID_VALUE";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public ResponseEntity<Object> handleBindException(BindException exception,
                                                      HttpHeaders headers,
                                                      HttpStatus status,
                                                      WebRequest request) {
        List<ErrorItemDto> errorItems = exception.getFieldErrors()
                .stream()
                .map(e -> new ErrorItemDto("FIELD_INVALID_FORMAT", e.getField(),
                        "Value '" + e.getRejectedValue() + "' is not allowed."))
                .collect(Collectors.toList());
        String message = "Validation of '" + exception.getObjectName() + "' failed.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.validationError(message, errorItems));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<ErrorItemDto> errorItems = exception.getBindingResult().getAllErrors().stream()
                .map(e -> {
                    String subject = (e instanceof FieldError) ? ((FieldError) e).getField() : e.getObjectName();
                    return new ErrorItemDto(FIELD_INVALID_VALUE, subject, e.getDefaultMessage());
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorDto.validationError("Validation of the request failed.", errorItems));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {
        return handleExceptionInternal(exception, status, getSubject(exception));
    }

    private String getSubject(TypeMismatchException exception) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            return ((MethodArgumentTypeMismatchException) exception).getName();
        } else {
            return exception.getPropertyName();
        }
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        return handleExceptionInternal(exception, status, null);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException exception,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatus status,
                                                                          final WebRequest request) {
        logException(exception);
        ErrorItemDto errorItem = new ErrorItemDto("QUERY_PARAMETER_MISSING", exception.getParameterName(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.validationError("Query parameter is missing.",
                Collections.singletonList(errorItem)));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.validationError("Validated object is missing."));
    }

    private <E extends Exception> ResponseEntity<Object> handleExceptionInternal(E exception,
                                                                                 HttpStatus status,
                                                                                 String subject) {
        logException(exception);
        List<ErrorItemDto> errorItems = null;
        if (subject != null) {
            errorItems = Collections.singletonList(new ErrorItemDto(FIELD_INVALID_VALUE, subject, null));
        }
        return ResponseEntity.status(status).body(new ErrorDto(exception.getClass().getName(), exception.getMessage(),
                errorItems));
    }

    private void logException(final Exception exception) {
        log.info("Handling {}: {}", exception.getClass().getName(), exception.getMessage());
    }
}
