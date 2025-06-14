package com.pickmylunch.api.domain.member.service;

import com.pickmylunch.api.domain.member.MemberProperties;
import com.pickmylunch.api.domain.member.repository.MemberRepository;
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

}
