package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.exceptions.ForbiddenException;
import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.exceptions.NotFoundException;
import com.amalitech.usermanagementservice.model.AuthToken;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.repository.AuthTokenRepository;
import com.amalitech.usermanagementservice.services.impl.AuthTokensServiceImp;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Epic("Authentication Tokens Management")
@Feature("AuthTokensService")
class AuthTokenServiceTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthTokensServiceImp authTokensService;

    private AuthToken authToken;
    private User user;

    @BeforeEach
    @Step("Set up test data for AuthTokensServiceImp tests")
    void setUp() {
        user = User.builder().id(1L).build();
        authToken = AuthToken.builder()
                .id(1L)
                .userId(user.getId())
                .token("testToken")
                .blacklisted(false)
                .build();
    }

    @Test
    @Story("Check if a token is blacklisted")
    @Description("Ensure an exception is thrown when a token is blacklisted")
    @Severity(SeverityLevel.CRITICAL)
    void throwIfTokenIsBlacklisted_ShouldThrowException_WhenTokenIsBlacklisted() {
        authToken.setBlacklisted(true);
        assertThrows(ForbiddenException.class, () -> authTokensService.throwIfTokenIsBlacklisted(authToken));
    }

    @Test
    @Story("Check if a token is blacklisted")
    @Description("Ensure no exception is thrown when a token is not blacklisted")
    @Severity(SeverityLevel.NORMAL)
    void throwIfTokenIsBlacklisted_ShouldNotThrowException_WhenTokenIsNotBlacklisted() {
        authToken.setBlacklisted(false);
        assertDoesNotThrow(() -> authTokensService.throwIfTokenIsBlacklisted(authToken));
    }

    @Test
    @Story("Save authentication token")
    @Description("Ensure a valid auth token is saved successfully")
    @Severity(SeverityLevel.CRITICAL)
    void saveAuthToken_ShouldReturnSavedToken() {
        when(authTokenRepository.save(authToken)).thenReturn(authToken);

        AuthToken savedToken = authTokensService.saveAuthToken(authToken);

        assertEquals(authToken, savedToken);
        verify(authTokenRepository, times(1)).save(authToken);
    }

    @Test
    @Story("Save authentication token")
    @Description("Throw an exception when saving an auth token fails")
    @Severity(SeverityLevel.CRITICAL)
    void saveAuthToken_ShouldThrowException_WhenRepositoryThrowsError() {
        when(authTokenRepository.save(authToken)).thenThrow(RuntimeException.class);

        assertThrows(InternalServerErrorException.class, () -> authTokensService.saveAuthToken(authToken));
    }

    @Test
    @Story("Retrieve authentication token by string")
    @Description("Return the auth token for a valid token string")
    @Severity(SeverityLevel.CRITICAL)
    void getToken_ShouldReturnAuthToken_WhenTokenExists() {
        when(authTokenRepository.findByToken("testToken")).thenReturn(Optional.of(authToken));

        AuthToken result = authTokensService.getToken("testToken");

        assertEquals(authToken, result);
        verify(authTokenRepository, times(1)).findByToken("testToken");
    }

    @Test
    @Story("Retrieve authentication token by string")
    @Description("Throw an exception when the token does not exist")
    @Severity(SeverityLevel.CRITICAL)
    void getToken_ShouldThrowException_WhenTokenDoesNotExist() {
        when(authTokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authTokensService.getToken("invalidToken"));
    }

    @Test
    @Story("Validate token")
    @Description("Ensure a token is validated successfully")
    @Severity(SeverityLevel.NORMAL)
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {
        when(jwtService.isTokenValid("testToken")).thenReturn(true);

        boolean isValid = authTokensService.validateToken("testToken");

        assertTrue(isValid);
        verify(jwtService, times(1)).isTokenValid("testToken");
    }

    @Test
    @Story("Validate token")
    @Description("Return false when the token is invalid")
    @Severity(SeverityLevel.NORMAL)
    void validateToken_ShouldReturnFalse_WhenTokenIsInvalid() {
        when(jwtService.isTokenValid("invalidToken")).thenReturn(false);

        boolean isValid = authTokensService.validateToken("invalidToken");

        assertFalse(isValid);
        verify(jwtService, times(1)).isTokenValid("invalidToken");
    }

    @Test
    @Story("Generate access token")
    @Description("Generate a new access token for a user and save it")
    @Severity(SeverityLevel.BLOCKER)
    void generateAccessToken_ShouldReturnGeneratedToken() {
        when(jwtService.generateAccessToken(user)).thenReturn("newTestToken");
        when(authTokenRepository.save(any(AuthToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthToken result = authTokensService.generateAccessToken(user);

        assertNotNull(result);
        assertEquals("newTestToken", result.getToken());
        assertFalse(result.isBlacklisted());
        verify(jwtService, times(1)).generateAccessToken(user);
        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    @Story("Delete authentication token by ID")
    @Description("Ensure a token is deleted by ID")
    @Severity(SeverityLevel.NORMAL)
    void deleteAuthToken_ShouldCallRepositoryDeleteById() {
        authTokensService.deleteAuthToken(1L);

        verify(authTokenRepository, times(1)).deleteById(1L);
    }

    @Test
    @Story("Delete authentication token by string")
    @Description("Ensure a token is deleted by token string")
    @Severity(SeverityLevel.NORMAL)
    void deleteAuthTokenByToken_ShouldCallRepositoryDeleteByToken() {
        authTokensService.deleteAuthToken("testToken");

        verify(authTokenRepository, times(1)).deleteByToken("testToken");
    }

    @Test
    @Story("Blacklist authentication token")
    @Description("Ensure a token is blacklisted")
    @Severity(SeverityLevel.NORMAL)
    void blacklistAccessToken_ShouldUpdateTokenBlacklistStatus() {
        authTokensService.blacklistAccessToken("testToken");

        verify(authTokenRepository, times(1)).updateTokenBlacklisted("testToken", true);
    }
}

