package com.pickmylunch.api.domain.member.controller;

import com.pickmylunch.api.domain.member.dto.request.*;
import com.pickmylunch.api.domain.member.dto.response.*;
import com.pickmylunch.api.domain.member.service.*;
import com.pickmylunch.api.global.security.details.AuthUser;
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
    public ResponseEntity<Void> saveRealTimeLocation(@RequestBody MemberLocationRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        memberLocationService.saveRealTimeLocation(dto, authUser.getId());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/real-time")
    public ResponseEntity<MemberLocationResponseDto> getRealTimeLocation(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(memberLocationService.getRealTimeLocation(authUser.getId()));
    }

    @PostMapping("/static")
    public ResponseEntity<Void> saveStaticLocation(@RequestBody StaticLocationRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        memberLocationService.saveStaticLocation(dto, authUser.getId());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/static")
    public ResponseEntity<List<StaticLocationResponseDto>> getAllStaticLocations(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(memberLocationService.getAllStaticLocations(authUser.getId()));
    }

    @GetMapping("/static/default")
    public ResponseEntity<StaticLocationResponseDto> getDefaultStaticLocation(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(memberLocationService.getDefaultStaticLocation(authUser.getId()));
    }

    @PatchMapping("/static/{locationId}/change-default")
    public ResponseEntity<Void> changeDefaultStaticLocation(@PathVariable Long locationId, @AuthenticationPrincipal AuthUser authUser, @RequestParam("is-default") boolean isDefault) {
        memberLocationService.changeDefaultStaticLocation(locationId, authUser.getId(), isDefault);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/static/{locationId}")
    public ResponseEntity<Void> updateStaticLocation(@PathVariable Long locationId, @AuthenticationPrincipal AuthUser authUser, @RequestBody StaticLocationUpdateRequestDto dto) {
        memberLocationService.updateStaticLocation(locationId, authUser.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/static/{locationId}")
    public ResponseEntity<Void> deleteStaticLocation(@PathVariable Long locationId, @AuthenticationPrincipal AuthUser authUser) {
        memberLocationService.deleteStaticLocation(locationId, authUser.getId());
        return ResponseEntity.noContent().build();
    }

}
