package com.pickmylunch.api.global.security.utils.cookie;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${cookie.cookie-name}")
    private String cookieName;

    @Value("${cookie.domain}")
    private String cookieDomain;

    @Value("${cookie.accepted-url}")
    private String cookieAcceptedUrl;

    @Value("${cookie.limit-time}")
    private int cookieLimitTime;

    public Cookie createCookie(String key) {
        Cookie cookie = new Cookie(cookieName, key);

        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setDomain(cookieDomain);
        cookie.setPath(cookieAcceptedUrl);
        cookie.setMaxAge(cookieLimitTime);
        return cookie;
    }
}
