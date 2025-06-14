package com.pickmylunch.api.domain.member.repository;

import com.pickmylunch.common.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


public interface MemberLocationRepository extends JpaRepository<MemberLocation, Long> {
    List<MemberLocation> findByMemberId(Long memberId);
    Optional<MemberLocation> findByMemberIdAndIsDefaultTrue(Long memberId);

    @Modifying
    @Transactional
    @Query("UPDATE MemberLocation ml SET ml.isDefault = false WHERE ml.member.id = :memberId AND ml.isDefault = true")
    void resetDefaultLocation(Long memberId);

}
