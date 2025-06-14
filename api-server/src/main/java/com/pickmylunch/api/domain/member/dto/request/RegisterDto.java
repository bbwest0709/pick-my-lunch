package com.pickmylunch.api.domain.member.dto.request;

import com.pickmylunch.api.domain.member.validation.EmailConstraint;
import com.pickmylunch.api.domain.member.validation.PasswordConstraint;
import com.pickmylunch.common.entity.Member;
import com.pickmylunch.common.entity.enums.MemberRole;

public record RegisterDto(String memberName,
                          @PasswordConstraint String password,
                          @EmailConstraint String email,
                          boolean recommendationOptIn) {
    public Member of(String password) {
        return Member.builder()
                .memberName(memberName)
                .password(password)
                .email(email())
                .role(MemberRole.USER)
                .recommendationOptIn(recommendationOptIn())
                .build();
    }
}