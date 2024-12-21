package com.amalitech.usermanagementservice.security.util;

import com.amalitech.usermanagementservice.dto.global.ErrorResponse;
import com.amalitech.usermanagementservice.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class FilterExceptionHandler {

    private final ObjectMapper objectMapper;

    public FilterExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleException(HttpServletResponse response, HttpServletRequest request, Exception exception)
            throws IOException {
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        String message = "Unauthorized: We were unable to authenticate your access token. This could be due to an expired or invalid token. Log in again to obtain a new access token.";

        ErrorResponse<String> errorResponse = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, message, Instant.now().toString(), request.getRequestURI(), exception.getMessage());

        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
