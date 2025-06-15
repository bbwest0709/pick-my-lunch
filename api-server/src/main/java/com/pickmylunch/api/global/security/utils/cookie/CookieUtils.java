package com.pickmylunch.api.global.security.utils.cookie;

import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final CookieProperties cookieProperties;

    public Cookie createCookie(String key) {
        Cookie cookie = new Cookie(cookieProperties.getCookieName(), key);

        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setDomain(cookieProperties.getDomain());
        cookie.setPath(cookieProperties.getAcceptedUrl());
        cookie.setMaxAge(cookieProperties.getLimitTime());
        return cookie;
    }

    public Cookie deleteCookie(HttpServletRequest request) {
        Cookie cookie = searchCookieProperties(validCookiesExist(request));
        cookie.setMaxAge(0);
        return cookie;
    }

    private Cookie searchCookieProperties(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieProperties.getCookieName()))
                .findFirst()
                .orElse(new Cookie(cookieProperties.getCookieName(), ""));
    }

    public Cookie searchCookieProperties(HttpServletRequest request) {
        return Arrays.stream(validCookiesExist(request))
                .filter(cookie -> cookie.getName().equals(cookieProperties.getCookieName()))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(AuthExceptionCode.REFRESH_TOKEN_EXPIRED));
    }

    private Cookie[] validCookiesExist(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies()).orElse(new Cookie[] {});
    }
}
