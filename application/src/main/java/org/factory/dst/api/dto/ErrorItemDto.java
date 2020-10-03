package org.factory.dst.api.dto;

/**
 * Data transfer object representing a single error item.
 */
public class ErrorItemDto {

    private String errorCode;
    private String subject;
    private String details;

    public ErrorItemDto(String errorCode, String subject, String details) {
        this.errorCode = errorCode;
        this.subject = subject;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
