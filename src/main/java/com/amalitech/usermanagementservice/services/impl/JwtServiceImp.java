package com.amalitech.usermanagementservice.services.impl;

import com.amalitech.usermanagementservice.config.properties.JwtPropertiesConfig;
import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImp implements JwtService {

  private final JwtPropertiesConfig jwtPropertiesConfig;

  public JwtServiceImp(JwtPropertiesConfig jwtPropertiesConfig) {
    this.jwtPropertiesConfig = jwtPropertiesConfig;
  }


  @Override
  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public boolean isTokenValid(
          String token
  ) {
    return extractAllClaims(token) != null;
  }


  @Override
  public String generateAccessToken(
          User user
  ) {
    try {
      Map<String, String> extraClaims = new HashMap<>();

      return generateAccessToken(extraClaims, user);
    } catch (Exception e) {
      throw new InternalServerErrorException("Error generating access token", e);
    }

  }

  public String generateAccessToken(Map<String, String> extraClaims, User user) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(user.getId().toString())
        .setIssuedAt(new Date())
        .setId(extraClaims.get("id"))
        .setExpiration(
            new Date(System.currentTimeMillis() + jwtPropertiesConfig.tokenExpiration()))
        .signWith(getSignKey(jwtPropertiesConfig.tokenSecretKey()))
        .compact();
  }

  public <T> T extractClaim(
      String token,
      Function<Claims, T> claimsResolver
  ) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {

    return Jwts.parserBuilder()
        .setSigningKey(getSignKey(jwtPropertiesConfig.tokenSecretKey()))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }


  private Key getSignKey(String secretKey) {

    return new SecretKeySpec(Base64.getDecoder()
        .decode(secretKey), SignatureAlgorithm.HS256.getJcaName());

  }
}
