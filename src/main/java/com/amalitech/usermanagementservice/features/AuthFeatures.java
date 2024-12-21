package com.amalitech.usermanagementservice.features;

import com.amalitech.usermanagementservice.dto.request.LoginRequestPayload;
import com.amalitech.usermanagementservice.dto.request.PasswordResetRequestPayload;
import com.amalitech.usermanagementservice.dto.response.PasswordResetResponsePayload;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;

public interface AuthFeatures {
    UserDetailsResponsePayload login(LoginRequestPayload payload);
    PasswordResetResponsePayload resetUserPassword(PasswordResetRequestPayload payload);
    void logout();
}
