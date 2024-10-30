package me.croco.onulmohaji.member.controller;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.NaverService;
import me.croco.onulmohaji.member.domain.MemberSearchInfo;
import me.croco.onulmohaji.member.dto.MemberAddRequest;
import me.croco.onulmohaji.member.dto.MemberSearchInfoFindResponse;
import me.croco.onulmohaji.member.dto.MemberSearchInfoUpdateRequest;
import me.croco.onulmohaji.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final NaverService naverService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberAddRequest request) {
        Long userId = memberService.saveMember(request);
        return userId != null
                ? ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(userId))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/api/memberSearchInfo/{memberId}")
    public ResponseEntity<MemberSearchInfoFindResponse> findMemberSearchInfo(@PathVariable Long memberId) {
        return ResponseEntity.ok()
                .body(memberService.findMemberSearchInfo(memberId));
    }

    @PutMapping("/api/memberSearchInfo/{memberId}")
    public ResponseEntity<MemberSearchInfoFindResponse> updateMemberSearchInfo(@RequestBody MemberSearchInfoUpdateRequest request) {
        memberService.updateMemberSearchInfo(request);
        return ResponseEntity.ok()
                .body(memberService.findMemberSearchInfo(request.getId()));
    }
}

