package com.amalitech.usermanagementservice.services.impl;

import com.amalitech.usermanagementservice.exceptions.ForbiddenException;
import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.exceptions.NotFoundException;
import com.amalitech.usermanagementservice.model.AuthToken;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.repository.AuthTokenRepository;
import com.amalitech.usermanagementservice.services.AuthTokensService;
import com.amalitech.usermanagementservice.services.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthTokensServiceImp implements AuthTokensService {

    private final AuthTokenRepository authTokenRepository;
    private final JwtService jwtService;


    public AuthTokensServiceImp(AuthTokenRepository authTokenRepository, JwtService jwtService) {
        this.authTokenRepository = authTokenRepository;
        this.jwtService = jwtService;
    }


    @Override
    public void throwIfTokenIsBlacklisted(AuthToken authToken) {
        boolean tokenBlacklisted = Optional.ofNullable(authToken)
                .orElseThrow(() -> new NotFoundException("Access token not found")).isBlacklisted();

        if (tokenBlacklisted) {
            throw new ForbiddenException("Access token has already been used");
        }
    }


    @Override
    public AuthToken saveAuthToken(AuthToken authToken) {
        try {
            return authTokenRepository.save(authToken);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not save auth token");
        }

    }

    @Override
    public AuthToken getToken(String token) {
        return authTokenRepository.findByToken(token).orElseThrow(
                () -> new NotFoundException("Access token not found")
        );
    }

    @Override
    public boolean validateToken(String token)  {
            return jwtService.isTokenValid(token);
    }

    @Override
    public AuthToken generateAccessToken(User user) {
        try{
            String accessToken = jwtService.generateAccessToken(user);
            AuthToken authToken =  AuthToken.builder()
                    .userId(user.getId())
                    .blacklisted(false)
                    .token(accessToken)
                    .build();
            return saveAuthToken(authToken);
        }catch(Exception e){
            throw new InternalServerErrorException("Error while generating tokens", e);
        }

    }

    @Override
    public AuthToken getAuthTokens(Long userId) {
        return authTokenRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Token not found"));
    }

    @Override
    @Transactional
    public void deleteAuthToken(Long id) {
        try {
            authTokenRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete auth tokens", e);
        }

    }

    @Override
    @Transactional
    public void deleteAuthToken(String accessToken) {
        try {
            authTokenRepository.deleteByToken(accessToken);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to revoke token", e);
        }

    }

    @Override
    @Transactional
    public void deleteUserAuthToken(Long userId) {
        try {
            authTokenRepository.deleteByUserId(userId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to revoke token", e);
        }

    }

    @Override
    @Transactional
    public void blacklistAccessToken(String accessToken) {
        try {
            authTokenRepository.updateTokenBlacklisted(accessToken, true);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to blacklist access token", e);
        }

    }

}
