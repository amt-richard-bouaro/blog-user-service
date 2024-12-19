package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import com.amalitech.usermanagementservice.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void ensureUsernameOrEmailNotTaken(String username, String email);

    User createUserObjectWithDefaultAvatar(RegistrationRequestPayload request);

    void activateUser(User user);

    void verifyCurrentPassword(String currentPassword, String password);

    User saveUser(User user);

    User getUserByUsernameOrEmail(String usernameOrEmail);

    void checkThatEmailAndUsernameDoesNotExist(@NotBlank String username, @NotBlank String email);

    void updatePassword(User user, String newPassword);

    User getUserByUsername(String username);

    User getUserById(Long id);

    // this really needs to be cached to improve performance
    boolean isUsernameTaken(String username);
    boolean isEmailAlreadyInUse(String email);

    Page<User> getUsers(Pageable pageable, String searchTerm, String status);
}
