package com.pickmylunch.api.global.security.utils.jwt;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private String prefix;
    private int accessTokenValidityInMinutes;
    private int refreshTokenValidityInMinutes;
}
