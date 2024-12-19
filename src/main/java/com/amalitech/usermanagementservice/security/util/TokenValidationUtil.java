package com.amalitech.usermanagementservice.security.util;

import com.amalitech.usermanagementservice.exceptions.UnauthorizedException;
import com.amalitech.usermanagementservice.services.AuthTokensService;
import com.amalitech.usermanagementservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationUtil {

    private final JwtService jwtService;
    private final AuthTokensService tokenService;

    public String validateAndExtractUserId(String token) {
        if (!jwtService.isTokenValid(token)) {
            tokenService.blacklistAccessToken(token);
            throw new UnauthorizedException("Invalid Token: Authentication token not valid");
        }

        try {
            return jwtService.extractSubject(token);
        } catch (Exception e) {
            throw new UnauthorizedException("An error occurred while extracting the token");
        }
    }

}
