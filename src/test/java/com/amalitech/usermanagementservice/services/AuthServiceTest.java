package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.exceptions.UnauthorizedException;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.services.impl.AuthServiceImpl;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Authentication Service Tests")
@Feature("Authentication Management")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Story("Authenticate user with valid credentials")
    @Description("Verify that authenticateUser sets SecurityContext authentication when credentials are valid")
    @Severity(SeverityLevel.CRITICAL)
    void authenticateUser_ShouldSetAuthentication_WhenCredentialsAreValid() {
        String username = "testuser";
        String password = "password";

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        authService.authenticateUser(username, password);

        verify(authenticationManager, times(1))
                .authenticate(argThat(auth ->
                        auth instanceof UsernamePasswordAuthenticationToken &&
                                auth.getName().equals(username) &&
                                auth.getCredentials().equals(password)
                ));
        verify(securityContext, times(1)).setAuthentication(authentication);
    }

    @Test
    @Story("Authenticate user with invalid credentials")
    @Description("Verify that authenticateUser throws UnauthorizedException when authentication fails")
    @Severity(SeverityLevel.CRITICAL)
    void authenticateUser_ShouldThrowUnauthorizedException_WhenAuthenticationFails() {
        String username = "invaliduser";
        String password = "invalidpassword";

        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new RuntimeException());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> authService.authenticateUser(username, password));

        assertEquals("Invalid username or password service", exception.getMessage());
    }

    @Test
    @Story("Retrieve authenticated user")
    @Description("Verify that getAuthenticatedUser returns the User object when the user is authenticated")
    @Severity(SeverityLevel.CRITICAL)
    void getAuthenticatedUser_ShouldReturnUser_WhenUserIsAuthenticated() {
        User mockUser = User.builder().id(1L).username("testuser").build();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        User authenticatedUser = authService.getAuthenticatedUser();

        assertNotNull(authenticatedUser);
        assertEquals("testuser", authenticatedUser.getUsername());
        verify(securityContext, times(1)).getAuthentication();
    }

    @Test
    @Story("Unauthorized user attempt")
    @Description("Verify that getAuthenticatedUser throws UnauthorizedException when the user is not authenticated")
    @Severity(SeverityLevel.CRITICAL)
    void getAuthenticatedUser_ShouldThrowUnauthorizedException_WhenUserIsNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, authService::getAuthenticatedUser);

        assertEquals("Unauthorized: User is not authorized", exception.getMessage());
    }

    @Test
    @Story("Invalid authentication principal")
    @Description("Verify that getAuthenticatedUser throws InternalServerErrorException when the principal cannot be cast to User")
    @Severity(SeverityLevel.NORMAL)
    void getAuthenticatedUser_ShouldThrowInternalServerErrorException_WhenPrincipalCannotBeCast() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("InvalidPrincipal");

        InternalServerErrorException exception = assertThrows(InternalServerErrorException.class, authService::getAuthenticatedUser);

        assertEquals("Failed to cast authentication principal to user", exception.getMessage());
    }

}
