package com.amalitech.usermanagementservice.exceptions;

public class InternalServerErrorException extends RuntimeException {
    private String message;

    public InternalServerErrorException(String message) {
        super(message);
        this.message = message;
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return this.message;
    }


}
