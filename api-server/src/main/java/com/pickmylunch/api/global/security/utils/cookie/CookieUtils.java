package com.pickmylunch.api.global.security.utils.cookie;

import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

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

    public Cookie deleteCookie(HttpServletRequest request) {
        Cookie cookie = searchCookieProperties(validCookiesExist(request));
        cookie.setMaxAge(0);
        return cookie;
    }

    private Cookie searchCookieProperties(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(new Cookie(cookieName, ""));
    }

    public Cookie searchCookieProperties(HttpServletRequest request) {
        return Arrays.stream(validCookiesExist(request))
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(AuthExceptionCode.REFRESH_TOKEN_EXPIRED));
    }

    private Cookie[] validCookiesExist(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies()).orElse(new Cookie[] {});
    }
}
