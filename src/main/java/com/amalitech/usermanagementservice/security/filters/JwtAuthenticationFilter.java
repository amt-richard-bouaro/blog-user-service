package com.amalitech.usermanagementservice.security.filters;

import com.amalitech.usermanagementservice.security.util.AuthenticationUtil;
import com.amalitech.usermanagementservice.security.util.FilterExceptionHandler;
import com.amalitech.usermanagementservice.security.util.TokenValidationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenValidationUtil tokenValidationUtil;
    private final AuthenticationUtil authenticationUtil;
    private final FilterExceptionHandler filterExceptionHandler;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7);
            //to be review later
            String userId = tokenValidationUtil.validateAndExtractUserId(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticationUtil.authenticateUser(Long.parseLong(userId), request);
            }

        } catch (Exception e) {
            filterExceptionHandler.handleException(response, request, e);
            return;
        }

        filterChain.doFilter(request, response);
    }


}
