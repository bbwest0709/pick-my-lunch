package com.pickmylunch.api.global.security.handler;

import com.pickmylunch.api.global.redis.RedisRepository;
import com.pickmylunch.api.global.security.utils.cookie.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LogoutSuccessCustomHandler implements LogoutSuccessHandler {

    private final CookieUtils cookieUtils;
    private final RedisRepository repository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        deleteTokenToRedis(cookieUtils.deleteCookie(request));
        createResponse(response);
    }

    private void deleteTokenToRedis(Cookie cookie) {
        repository.delete(cookie.getValue());
    }

    private void createResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().flush();
    }
}
