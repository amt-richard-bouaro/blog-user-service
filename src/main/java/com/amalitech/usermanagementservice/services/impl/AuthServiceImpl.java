package com.amalitech.usermanagementservice.services.impl;

import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.exceptions.UnauthorizedException;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final AuthenticationManager authenticationManager;

  @Override
  public void authenticateUser(String username, String password) {

    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password)
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);

    }catch (RuntimeException e) {
      throw new UnauthorizedException("Invalid username or password service");
    }
  }


  @Override
  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!authentication.isAuthenticated()) {
      throw new UnauthorizedException("Unauthorized: User is not authorized");
    }

    Object principal = authentication.getPrincipal();

    try {
      return (User) principal;
    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to cast authentication principal to user");
    }

  }


}
