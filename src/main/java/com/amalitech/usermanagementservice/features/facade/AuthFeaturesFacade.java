package com.amalitech.usermanagementservice.features.facade;

import com.amalitech.usermanagementservice.dto.request.LoginRequestPayload;
import com.amalitech.usermanagementservice.dto.request.PasswordResetRequestPayload;
import com.amalitech.usermanagementservice.dto.response.PasswordResetResponsePayload;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;
import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.features.AuthFeatures;
import com.amalitech.usermanagementservice.model.AuthToken;
import com.amalitech.usermanagementservice.model.Mapper.UseDetailsResponseModelMapper;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.notification.EmailNotification;
import com.amalitech.usermanagementservice.services.AuthService;
import com.amalitech.usermanagementservice.services.AuthTokensService;
import com.amalitech.usermanagementservice.services.SharedService;
import com.amalitech.usermanagementservice.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFeaturesFacade implements AuthFeatures {

    private final UserService userService;

    private final AuthService authService;

    private final AuthTokensService authTokensService;

    private final EmailNotification emailNotification;

    private final SharedService sharedService;

    private final UseDetailsResponseModelMapper useDetailsResponseModelMapper;


    @Override
    public UserDetailsResponsePayload login(LoginRequestPayload payload) {

        if (payload == null)
            throw new InternalServerErrorException("Payload is empty");

        authService.authenticateUser(payload.username(), payload.password());

        User authenticatedUser = authService.getAuthenticatedUser();

        AuthToken authToken = authTokensService.generateAccessToken(authenticatedUser);

        UserDetailsResponsePayload response = useDetailsResponseModelMapper.mapToDto(authenticatedUser);

        response.setToken(authToken.getToken());

        return response;
    }

    @Override
    public PasswordResetResponsePayload resetUserPassword(PasswordResetRequestPayload payload) {

        User user = userService.getUserByUsername(payload.username());

        String randomPassword = sharedService.generateRandomPassword(8);

        userService.updatePassword(user, randomPassword);

        emailNotification.handlePasswordResetNotification(user.getEmail(), user.getUsername(), randomPassword);

        return new PasswordResetResponsePayload(true, "New password has been sent to this email " + user.getEmail());
    }


    @Override
    @Transactional
    public void logout() {

        User user = authService.getAuthenticatedUser();

        authTokensService.deleteUserAuthToken(user.getId());

    }
}
