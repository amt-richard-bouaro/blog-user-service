package com.amalitech.usermanagementservice.security.util;


import com.amalitech.usermanagementservice.exceptions.NotFoundException;
import com.amalitech.usermanagementservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUtil {
  private final UserDetailsService userDetailsService;

  private final UserRepository userRepository;

  private UserDetails getUserDetails(Long userId) {
    return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public void authenticateUser(Long userId, HttpServletRequest request) {
    UserDetails userDetails =getUserDetails(userId);
    if (userDetails != null) {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
  }

}