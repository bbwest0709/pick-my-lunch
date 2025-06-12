package com.pickmylunch.api.global.security.service;

import com.pickmylunch.api.domain.member.repository.*;
import com.pickmylunch.api.global.exception.*;
import com.pickmylunch.api.global.exception.code.*;
import com.pickmylunch.api.global.redis.RedisRepository;
import com.pickmylunch.api.global.security.dto.*;
import com.pickmylunch.api.global.security.utils.ObjectMapperUtils;
import com.pickmylunch.api.global.security.utils.cookie.CookieUtils;
import com.pickmylunch.api.global.security.utils.jwt.JwtProvider;
import com.pickmylunch.common.entity.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;
    private final ObjectMapperUtils objectMapper;
    private final RedisRepository redisRepository;
    private final MemberRepository memberRepository;

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        Member findMember = validRefreshTokenSubject(findMemberData(request));

        String refresh = getRefreshToken(findMember);
        String access = getAccessToken(findMember, findMemberData(request));

        saveMemberInfoToRedis(refresh, findMemberData(request));

        response.setHeader(HttpHeaders.AUTHORIZATION, access);
        response.addCookie(cookieUtils.createCookie(refresh));
    }

    private MemberInfo findMemberData(HttpServletRequest request) {
        return findMemberInfo(cookieUtils.searchCookieProperties(request));
    }

    private MemberInfo findMemberInfo(Cookie refreshCookie) {
        return objectMapper.toEntity(findAndDeleteToRedis(refreshCookie), MemberInfo.class);
    }

    private String findAndDeleteToRedis(Cookie refreshCookie) {
        String tokenToRedis = findTokenToRedis(refreshCookie);
        deleteToken(tokenToRedis);
        return tokenToRedis;
    }

    private String findTokenToRedis(Cookie refreshCookie) {
        return Optional.ofNullable(redisRepository.findByKey(refreshCookie.getValue()))
                .orElseThrow(() -> new BusinessLogicException(AuthExceptionCode.REFRESH_TOKEN_NOT_FOUND));
    }

    private void deleteToken(String tokenToRedis) {
        redisRepository.delete(tokenToRedis);
    }

    private Member validRefreshTokenSubject(MemberInfo memberInfo) {
        return memberRepository.findByMemberName(memberInfo.memberName())
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

    private String getRefreshToken(Member findMember) {
        return jwtProvider.generateRefreshToken(findMember.getMemberName());
    }

    private String getAccessToken(Member findMember, MemberInfo memberInfo) {
        return jwtProvider.getPrefix() + jwtProvider.generateAccessToken(findMember.getMemberName(), findMember.getId(), memberInfo.authorities());
    }

    private void saveMemberInfoToRedis(String refresh, MemberInfo memberInfo) {
        redisRepository.save(
                refresh,
                objectMapper.toStringValue(memberInfo),
                jwtProvider.getRefreshTokenValidityInMinutes()
        );
    }
}