package com.pickmylunch.api.domain.member.service;

import com.pickmylunch.api.domain.member.dto.request.*;
import com.pickmylunch.api.domain.member.dto.response.*;
import com.pickmylunch.api.domain.member.repository.*;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.MemberExceptionCode;
import com.pickmylunch.api.global.redis.RedisRepository;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLocationService {

    private final MemberLocationRepository memberLocationRepository;
    private final MemberRepository memberRepository;
    private final RedisRepository redis;

    public void saveRealTimeLocation(MemberLocationRequestDto dto, Long memberId) {
        getMemberById(memberId);
        redis.saveRealTimeLocation(memberId, dto.lon(), dto.lat());
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

}
