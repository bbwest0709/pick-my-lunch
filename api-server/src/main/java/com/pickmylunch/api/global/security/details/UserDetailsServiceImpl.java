package com.pickmylunch.api.global.security.details;

import com.pickmylunch.api.domain.member.repository.MemberRepository;
import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import com.pickmylunch.common.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(AuthExceptionCode.USER_NOT_FOUND.getMessage()));
        return new AuthUser(findMember);
    }
}
