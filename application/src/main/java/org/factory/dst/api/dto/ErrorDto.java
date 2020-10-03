package org.factory.dst.api.dto;

import java.util.List;

/**
 * Data transfer object representing an error response.
 */
public class ErrorDto {

    private String errorCode;
    private String errorDescription;
    private List<ErrorItemDto> errors;

    public ErrorDto(String errorCode, String errorDescription, List<ErrorItemDto> errors) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public List<ErrorItemDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorItemDto> errors) {
        this.errors = errors;
    }

    /**
     * Create validation error.
     *
     * @param message Message.
     * @return New instance.
     */
    public static ErrorDto validationError(final String message) {
        return validationError(message, null);
    }

    /**
     * Create validation error.
     *
     * @param message Message.
     * @param errors Validation errors.
     * @return New instance.
     */
    public static ErrorDto validationError(final String message, final List<ErrorItemDto> errors) {
        return new ErrorDto("VALIDATION_ERROR", message, errors);
    }

    /**
     * Create not found error.
     *
     * @param message Message.
     * @return New instance.
     */
    public static ErrorDto notFoundError(final String message) {
        return new ErrorDto("NOT_FOUND", message, null);
    }

    /**
     * Create general error.
     *
     * @param message Message.
     * @return New instance.
     */
    public static ErrorDto generalError(final String message) {
        return new ErrorDto("INTERNAL_ERROR", message, null);
    }
}
