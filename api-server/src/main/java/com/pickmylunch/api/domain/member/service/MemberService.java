package com.pickmylunch.api.domain.member.service;

import com.pickmylunch.api.domain.member.MemberProperties;
import com.pickmylunch.api.domain.member.dto.request.*;
import com.pickmylunch.api.domain.member.dto.response.*;
import com.pickmylunch.api.domain.member.repository.MemberRepository;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.MemberExceptionCode;
import com.pickmylunch.common.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberProperties memberProperties;
    private final PasswordEncoder passwordEncoder;

    public boolean isMemberNameExist(String memberName) {
        return memberRepository.existsByMemberName(memberName);
    }

    public boolean isEmailExist(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void register(RegisterDto dto) {
        Member member = dto.of(passwordEncoder.encode(dto.password()));
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(Long id) {
        return MemberResponseDto.of(getMemberById(id));
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

    public void updateRecommendationAlertsEnabled(long id, boolean enabled) {
        Member member = getMemberById(id);
        member.updateRecommendationOptIn(enabled);
        memberRepository.save(member);
    }

    public void deactivateMember(long id) {
        Member member = getMemberById(id);
        member.deactivate();
        member.updateRecommendationOptIn(false);
        member.anonymize(memberProperties.getAnonymizedName(), memberProperties.getAnonymizedEmail());
        memberRepository.save(member);
    }
}
