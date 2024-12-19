package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.model.AuthToken;
import com.amalitech.usermanagementservice.model.User;
import jakarta.transaction.Transactional;

public interface AuthTokensService {
    void throwIfTokenIsBlacklisted(AuthToken authToken);

    AuthToken saveAuthToken(AuthToken authToken);

    AuthToken getToken(String token);

    boolean validateToken(String token);

    AuthToken generateAccessToken(User user);

    AuthToken getAuthTokens(Long userId);

    @Transactional
    void deleteAuthToken(Long id);

    @Transactional
    void deleteAuthToken(String accessToken);

    void deleteUserAuthToken(Long userId);

    @Transactional
    void blacklistAccessToken(String accessToken);
}
