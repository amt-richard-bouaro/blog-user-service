package com.amalitech.usermanagementservice.security;

import com.amalitech.usermanagementservice.dto.global.ErrorResponse;
import com.amalitech.usermanagementservice.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        boolean isAccessDenied = authException != null;

        int statusCode = isAccessDenied ? HttpStatus.FORBIDDEN.value() : HttpStatus.SERVICE_UNAVAILABLE.value();
        String message = isAccessDenied
                ? "Access Denied: You are not authorized to access this resource."
                : "Service Unavailable: An unexpected error occurred. Please try again later.";
        String errorTraceMessage = isAccessDenied
                ? "Access to the protected path was denied due to insufficient privileges or missing authentication."
                : "The service encountered an unexpected issue. Check logs for unhandled exceptions or system errors.";

        ErrorResponse<String> errorResponse = new ErrorResponse<>(
                ErrorCode.ERR_UNKNOWN,
                message,
                Instant.now().toString(),
                request.getRequestURI(),
                errorTraceMessage);

        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));


    }
}
