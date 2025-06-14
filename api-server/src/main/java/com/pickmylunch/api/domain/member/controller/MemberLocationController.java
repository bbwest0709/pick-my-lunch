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


}
