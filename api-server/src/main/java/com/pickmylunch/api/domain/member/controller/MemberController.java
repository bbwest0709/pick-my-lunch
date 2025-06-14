package com.pickmylunch.api.domain.member.controller;

import com.pickmylunch.api.domain.member.dto.request.*;
import com.pickmylunch.api.domain.member.dto.response.*;
import com.pickmylunch.api.domain.member.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto dto) {
        memberService.register(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/exists/member-name")
    public ResponseEntity<Boolean> checkDuplicateMemberName(@RequestParam String memberName) {
        boolean exists = memberService.isMemberNameExist(memberName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean exists = memberService.isEmailExist(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(@AuthenticationPrincipal Long id) {
        return ResponseEntity.ok(memberService.getMemberInfo(id));
    }

    @PatchMapping("/me/alerts/recommendation")
    public ResponseEntity<Void> updateRecommendationAlerts(@AuthenticationPrincipal Long id, @RequestParam("enabled") boolean enabled) {
        memberService.updateRecommendationAlertsEnabled(id, enabled);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> withdrawMember(@AuthenticationPrincipal Long id) {
        memberService.deactivateMember(id);
        return ResponseEntity.noContent().build();
    }
}