package com.amalitech.usermanagementservice.services;

public interface SharedService {
    String generateProfileAvatarUrl(String name);

    void validateCurrentPassword(
            String providedPassword,
            String encodedPassword
    );

    String getAccessTokenFromHeader(String authorizationHeader);

    String generateRandomPassword(int length);
}
