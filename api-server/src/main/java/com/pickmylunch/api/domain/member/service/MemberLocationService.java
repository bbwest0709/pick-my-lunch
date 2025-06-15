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

    public MemberLocationResponseDto getRealTimeLocation(Long memberId) {
        getMemberById(memberId);
        Point point = redis.getRealTimeLocation(memberId);
        return new MemberLocationResponseDto(point.getX(), point.getY());
    }

    public void saveStaticLocation(StaticLocationRequestDto dto, Long memberId) {
        resetMemberDefaultLocation(memberId);
        MemberLocation newLocation = createLocation(dto, memberId);
        newLocation.changeIsDefault(true);
        memberLocationRepository.save(newLocation);
    }

    public List<StaticLocationResponseDto> getAllStaticLocations(Long memberId) {
        return findMemberLocations(memberId).stream()
                .map(StaticLocationResponseDto::of)
                .toList();
    }

    public StaticLocationResponseDto getDefaultStaticLocation(Long memberId) {
        MemberLocation defaultLocation = findDefaultLocation(memberId);
        return StaticLocationResponseDto.of(defaultLocation);
    }

    public void changeDefaultStaticLocation(Long locationId, Long memberId) {
        resetMemberDefaultLocation(memberId);
        MemberLocation locationToBeDefault = findByIdAndMemberId(locationId, memberId);
        locationToBeDefault.changeIsDefault(true);
        memberLocationRepository.save(locationToBeDefault);
    }

    public void updateStaticLocation(Long locationId, Long memberId, StaticLocationUpdateRequestDto dto) {
        MemberLocation location = findByIdAndMemberId(locationId, memberId);
        location.update(dto.name(), dto.lat(), dto.lon());
        memberLocationRepository.save(location);
    }

    public void deleteStaticLocation(Long locationId, Long memberId) {
        MemberLocation location = findByIdAndMemberId(locationId, memberId);
        memberLocationRepository.delete(location);
    }

    private void resetMemberDefaultLocation(Long memberId) {
        memberLocationRepository.resetDefaultLocation(memberId);
    }

    private MemberLocation createLocation(StaticLocationRequestDto dto, Long memberId) {
        Member member = getMemberById(memberId);
        return dto.of(member);
    }

    private MemberLocation findByIdAndMemberId(Long locationId, Long memberId) {
        return memberLocationRepository.findByIdAndMemberId(locationId, memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.LOCATION_NOT_FOUNT));
    }

    private List<MemberLocation> findMemberLocations(Long memberId) {
        List<MemberLocation> locations = memberLocationRepository.findByMemberId(memberId);
        checkIfLocationsExist(locations);
        return locations;
    }

    private void checkIfLocationsExist(List<MemberLocation> locations) {
        if (locations.isEmpty()) {
            throw new BusinessLogicException(MemberExceptionCode.LOCATION_NOT_FOUNT);
        }
    }

    private MemberLocation findDefaultLocation(Long memberId) {
        return memberLocationRepository.findByMemberIdAndIsDefaultTrue(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.DEFAULT_LOCATION_NOT_FOUND));
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }
}