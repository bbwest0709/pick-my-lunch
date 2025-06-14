package com.pickmylunch.api.domain.member.controller;

import com.pickmylunch.api.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

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
}