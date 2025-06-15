package com.pickmylunch.api.global.security.utils.cookie;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cookie")
public class CookieProperties {
    private String cookieName;
    private String domain;
    private String acceptedUrl;
    private int limitTime;
}
