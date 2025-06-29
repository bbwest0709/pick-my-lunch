package com.pickmylunch.api.domain.member.controller;

import com.pickmylunch.api.domain.member.dto.request.*;
import com.pickmylunch.api.domain.member.dto.response.*;
import com.pickmylunch.api.domain.member.service.*;
import com.pickmylunch.api.global.security.details.*;
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
    public ResponseEntity<Boolean> checkDuplicateMemberName(@RequestParam("member-name") String memberName) {
        return ResponseEntity.ok(memberService.isMemberNameExist(memberName));
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(memberService.isEmailExist(email));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(@AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(memberService.getMemberInfo(user.getId()));
    }

    @PatchMapping("/me/alerts/recommendation")
    public ResponseEntity<Void> updateRecommendationAlerts(@AuthenticationPrincipal AuthUser user, @RequestParam("enabled") boolean enabled) {
        memberService.updateRecommendationAlertsEnabled(user.getId(), enabled);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> withdrawMember(@AuthenticationPrincipal AuthUser user) {
        memberService.deactivateMember(user.getId());
        return ResponseEntity.noContent().build();
    }
}