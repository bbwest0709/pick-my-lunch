package com.pickmylunch.api.domain.member.dto.request;

import com.pickmylunch.api.domain.member.validation.EmailConstraint;
import com.pickmylunch.api.domain.member.validation.PasswordConstraint;
import com.pickmylunch.common.entity.Member;
import com.pickmylunch.common.entity.enums.MemberRole;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterDto(
        String memberName,
        @PasswordConstraint String password,
        @EmailConstraint String email,
        boolean recommendationOptIn
) {
    public Member of(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .memberName(memberName)
                .password(passwordEncoder.encode(password()))
                .email(email())
                .role(MemberRole.USER)
                .recommendationOptIn(recommendationOptIn())
                .build();
    }
}