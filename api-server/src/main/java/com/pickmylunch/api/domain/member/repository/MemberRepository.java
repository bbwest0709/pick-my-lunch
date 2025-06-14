package com.pickmylunch.api.domain.member.repository;

import com.pickmylunch.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String username);
    Optional<Member> findByMemberName(String username);

    boolean existsByMemberName(String memberName);

}
