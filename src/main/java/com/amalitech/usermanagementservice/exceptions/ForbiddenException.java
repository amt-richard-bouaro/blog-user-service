package com.amalitech.usermanagementservice.exceptions;

public class ForbiddenException extends RuntimeException {
    private final String message;

    public ForbiddenException(String message) {
        this.message = message;
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
