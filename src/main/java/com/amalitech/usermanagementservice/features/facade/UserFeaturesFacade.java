package com.amalitech.usermanagementservice.features.facade;


import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import com.amalitech.usermanagementservice.dto.request.UpdatePasswordRequestPayload;
import com.amalitech.usermanagementservice.dto.request.UpdateProfileRequestPayload;
import com.amalitech.usermanagementservice.dto.response.PasswordResetResponsePayload;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;
import com.amalitech.usermanagementservice.exceptions.ConflictException;
import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.features.UserFeatures;
import com.amalitech.usermanagementservice.model.AuthToken;
import com.amalitech.usermanagementservice.model.Mapper.UseDetailsResponseModelMapper;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.notification.EmailNotification;
import com.amalitech.usermanagementservice.services.AuthService;
import com.amalitech.usermanagementservice.services.AuthTokensService;
import com.amalitech.usermanagementservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFeaturesFacade implements UserFeatures {

    private final UserService userService;

    private final AuthService authService;

    private final AuthTokensService authTokensService;

    private final UseDetailsResponseModelMapper useDetailsResponseModelMapper;


    @Override
    public UserDetailsResponsePayload registerUser(RegistrationRequestPayload payload) {

        if (payload == null) throw new InternalServerErrorException("Payload is empty");

        userService.checkThatEmailAndUsernameDoesNotExist(payload.username(), payload.email());

        User savedUser = userService.saveUser(userService.createUserObjectWithDefaultAvatar(payload));

        AuthToken authToken = authTokensService.generateAccessToken(savedUser);

        UserDetailsResponsePayload response = useDetailsResponseModelMapper.mapToDto(savedUser);

        response.setToken(authToken.getToken());

        return response;
    }

    public UserDetailsResponsePayload getAuthenticatedUserProfile() {

        User authenticatedUser = authService.getAuthenticatedUser();

        return useDetailsResponseModelMapper.mapToDto(authenticatedUser);
    }

    @Override
    public UserDetailsResponsePayload updateUserProfile(UpdateProfileRequestPayload payload) {

        User user = authService.getAuthenticatedUser();

        if (!user.getUsername().equals(payload.username()) && userService.isUsernameTaken(payload.username())) {
            throw new ConflictException("Username already taken");
        }

        if (!user.getEmail().equals(payload.email()) && userService.isEmailAlreadyInUse(payload.email())) {
            throw new ConflictException("Email already in use");
        }

        user.setName(payload.name());
        user.setEmail(payload.email());
        user.setPhone(payload.phone());
        user.setUsername(payload.username());

        User savedUser = userService.saveUser(user);

        return useDetailsResponseModelMapper.mapToDto(savedUser);
    }

    @Override
    public void updateUserPassword(UpdatePasswordRequestPayload payload) {

        User user = authService.getAuthenticatedUser();

        userService.verifyCurrentPassword(user.getPassword(), payload.currentPassword());

        userService.updatePassword(user, payload.newPassword());

    }



}
