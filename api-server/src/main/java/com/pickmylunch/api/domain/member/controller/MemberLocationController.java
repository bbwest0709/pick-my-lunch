package com.pickmylunch.api.domain.member.controller;

import com.pickmylunch.api.domain.member.dto.request.*;
import com.pickmylunch.api.domain.member.dto.response.*;
import com.pickmylunch.api.domain.member.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/members/locations")
@RequiredArgsConstructor
public class MemberLocationController {

    private final MemberLocationService memberLocationService;

    @PostMapping("/real-time")
    public ResponseEntity<Void> saveRealTimeLocation(@RequestBody MemberLocationRequestDto dto, @AuthenticationPrincipal Long memberId) {
        memberLocationService.saveRealTimeLocation(dto, memberId);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/real-time")
    public ResponseEntity<MemberLocationResponseDto> getRealTimeLocation(@AuthenticationPrincipal Long memberId) {
        MemberLocationResponseDto location = memberLocationService.getRealTimeLocation(memberId);
        return ResponseEntity.ok(location);
    }

    @PostMapping("/static")
    public ResponseEntity<Void> saveStaticLocation(@RequestBody StaticLocationRequestDto dto, @AuthenticationPrincipal Long memberId) {
        memberLocationService.saveStaticLocation(dto, memberId);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/static")
    public ResponseEntity<List<StaticLocationResponseDto>> getAllStaticLocations(@AuthenticationPrincipal Long memberId) {
        List<StaticLocationResponseDto> locations = memberLocationService.getAllStaticLocations(memberId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/static/default")
    public ResponseEntity<StaticLocationResponseDto> getDefaultStaticLocation(@AuthenticationPrincipal Long memberId) {
        StaticLocationResponseDto location = memberLocationService.getDefaultStaticLocation(memberId);
        return ResponseEntity.ok(location);
    }

    @PatchMapping("/static/{locationId}/change-default")
    public ResponseEntity<Void> changeDefaultStaticLocation(@PathVariable Long locationId, @AuthenticationPrincipal Long memberId) {
        memberLocationService.changeDefaultStaticLocation(locationId, memberId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/static/{locationId}")
    public ResponseEntity<Void> updateStaticLocation(@PathVariable Long locationId, @AuthenticationPrincipal Long memberId, @RequestBody StaticLocationUpdateRequestDto dto) {
        memberLocationService.updateStaticLocation(locationId, memberId, dto);
        return ResponseEntity.ok().build();
    }

}
