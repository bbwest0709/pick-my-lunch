package com.pickmylunch.api.domain.member.dto.response;

import com.pickmylunch.common.entity.Member;

public record MemberResponseDto(String memberName, String email, boolean launchRecommendAgree) {
    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(
                member.getMemberName(),
                member.getEmail(),
                member.isRecommendationOptIn()
        );
    }
}