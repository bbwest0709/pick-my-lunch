package com.pickmylunch.api.global.security.utils.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String subject, Long id, String authorities) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(getAccessExpiration())
                .addClaims(createClaims(id, authorities))
                .signWith(getEncodeKey())
                .compact();
    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(getRefreshExpiration())
                .signWith(getEncodeKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getEncodeKey())
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getEncodeKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, Object> createClaims(Long id, String authorities) {
        return Map.of("id", id, "authorities", authorities);
    }

    private Date getAccessExpiration() {
        return addExpirationData(jwtProperties.getAccessTokenValidityInMinutes()).getTime();
    }

    private Date getRefreshExpiration() {
        return addExpirationData(jwtProperties.getRefreshTokenValidityInMinutes()).getTime();
    }

    private Calendar addExpirationData(int expirationMinutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, expirationMinutes);
        return now;
    }

    public int getRefreshTokenValidityInMinutes() {
        return jwtProperties.getRefreshTokenValidityInMinutes();
    }

    public String getPrefix() {
        return jwtProperties.getPrefix();
    }
}