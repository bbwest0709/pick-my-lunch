package com.pickmylunch.api.global.security.utils.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private int accessTokenValidity;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private int refreshTokenValidity;

    public String generateAccessToken(String subject, Long id, String authorities) {
        return Jwts.builder()
                .subject(subject)
                .expiration(getAccessExpiration())
                .claims(createClaims(id, authorities))
                .signWith(getEncodeKey())
                .compact();
    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .expiration(getRefreshExpiration())
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
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, ?> createClaims(Long id, String authorities) {
        return Map.of("id", id, "authorities", authorities);
    }

    private Date getAccessExpiration() {
        return addExpirationData(accessTokenValidity).getTime();
    }

    private Date getRefreshExpiration() {
        return addExpirationData(refreshTokenValidity).getTime();
    }

    private Calendar addExpirationData(Integer expirationMinutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, expirationMinutes);
        return now;
    }

}