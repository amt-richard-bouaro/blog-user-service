package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.model.User;
import io.jsonwebtoken.Claims;

import java.util.Date;

public interface JwtService {
    String extractSubject(String token);

    boolean isTokenValid(
            String token
    );

    String generateAccessToken(
            User user
    );
}
