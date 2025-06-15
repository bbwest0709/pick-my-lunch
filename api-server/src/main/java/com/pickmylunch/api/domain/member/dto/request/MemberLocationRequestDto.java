package com.pickmylunch.api.domain.member.dto.request;

public record MemberLocationRequestDto(
        Double lat,  // 위도
        Double lon // 경도
) {}