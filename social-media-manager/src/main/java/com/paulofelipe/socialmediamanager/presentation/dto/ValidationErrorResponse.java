package com.paulofelipe.socialmediamanager.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<FieldError> errors;

    public ValidationErrorResponse(int status, String message, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<FieldError> getErrors() { return errors; }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public String getMessage() { return message; }
    }
}
