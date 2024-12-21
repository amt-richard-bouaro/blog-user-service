package com.amalitech.usermanagementservice.exceptions;

public class UnauthorizedException extends RuntimeException {
    
    private final String message;
    
    public UnauthorizedException(String message) {
        this.message = message;
    }
    
    public UnauthorizedException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.message = message;
        
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    
}
