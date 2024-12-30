package com.amalitech.usermanagementservice.services.impl;

import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import com.amalitech.usermanagementservice.exceptions.*;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.repository.UserRepository;
import com.amalitech.usermanagementservice.services.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void ensureUsernameOrEmailNotTaken(String username, String email) {
        userRepository.findByUsernameOrEmail(username, email).ifPresent(user -> {
            String message =
                    user.getUsername().equals(username) ? "Username already in use" : "Email already in use";
            throw new ConflictException(message);
        });
    }

    @Override
    public User createUserObjectWithDefaultAvatar(RegistrationRequestPayload request) {
        String encodedName = URLEncoder.encode(request.name(), StandardCharsets.UTF_8);
        String avatarUrl = "https://eu.ui-avatars.com/api/?name=" + encodedName + "&size=250";

        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .name(request.name())
                .picture(avatarUrl)
                .active(true)
                .build();
    }

    @Override
    public void activateUser(User user) {
        if (!user.isActive()) {
            user.setActive(true);
            saveUser(user);
        }
    }


    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);

        } catch (Exception e) {
            throw new InternalServerErrorException("Could not save user", e.getCause());
        }
    }

    @Override
    public void verifyCurrentPassword(String existingPassword, String password) {
        if(!passwordEncoder.matches(password, existingPassword)){
            throw new ForbiddenException("Password incorrect");
        }
    }


    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        saveUser(user);
    }


    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username '" + username + "' not found"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id '" + id + "' not found"));
    }


    // this really needs to be cached to improve performance
    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailAlreadyInUse(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void checkThatEmailAndUsernameDoesNotExist(@NotBlank String username, @NotBlank String email) {

        if (userRepository.existsByUsername(username.trim())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(email.trim())) {
            throw new ConflictException("Email already exists");
        }

    }

    @Override
    public Page<User> getUsers(Pageable pageable, String searchTerm, String status) {

        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            return userRepository.findByWithSearchAndSortAndFilters(searchTerm, status, pageable);
        }

        return userRepository.findAll(pageable);
    }


}

