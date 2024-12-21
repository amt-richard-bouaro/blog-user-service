package com.amalitech.usermanagementservice.services.impl;


import com.amalitech.usermanagementservice.exceptions.BadRequestException;
import com.amalitech.usermanagementservice.exceptions.NotFoundException;
import com.amalitech.usermanagementservice.services.SharedService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
public class SharedServicesImpl implements SharedService {

    private final PasswordEncoder passwordEncoder;


    public SharedServicesImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String generateProfileAvatarUrl(String name) {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        return "https://eu.ui-avatars.com/api/?name=" + encodedName + "&size=250";
    }

    @Override
    public void validateCurrentPassword(
            String providedPassword,
            String encodedPassword
    ) {
        if (!passwordEncoder.matches(providedPassword, encodedPassword)) {
            throw new BadRequestException("Current password is incorrect");
        }
    }

    @Override
    public String getAccessTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotFoundException("No access token found");
        }

        return authorizationHeader.substring(7);
    }

    @Override
    public String generateRandomPassword(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$&!";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }

        return password.toString();
    }


}
