package com.amalitech.usermanagementservice.features;

import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import com.amalitech.usermanagementservice.dto.request.UpdatePasswordRequestPayload;
import com.amalitech.usermanagementservice.dto.request.UpdateProfileRequestPayload;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;

public interface UserFeatures {
    UserDetailsResponsePayload registerUser(RegistrationRequestPayload payload);

    UserDetailsResponsePayload getAuthenticatedUserProfile();

    UserDetailsResponsePayload updateUserProfile(UpdateProfileRequestPayload payload);

    void updateUserPassword(UpdatePasswordRequestPayload payload);
    UserDetailsResponsePayload getUserProfile(Long id);
}
