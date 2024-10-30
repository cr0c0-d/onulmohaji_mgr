package me.croco.onulmohaji.place.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.member.service.MemberService;
import me.croco.onulmohaji.place.domain.CustomPlace;
import me.croco.onulmohaji.place.dto.CustomPlaceAddRequest;
import me.croco.onulmohaji.place.dto.CustomPlaceAddResponse;
import me.croco.onulmohaji.place.dto.CustomPlaceListFindResponse;
import me.croco.onulmohaji.place.dto.CustomPlaceUpdateRequest;
import me.croco.onulmohaji.place.service.CustomPlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomPlaceController {

    private final CustomPlaceService customPlaceService;
    private final MemberService memberService;

    @PostMapping("/api/customPlace")
    public ResponseEntity<CustomPlaceAddResponse> addCustomPlace(@RequestBody CustomPlaceAddRequest addRequest, HttpServletRequest request) {
        Member loginMember = memberService.getLoginMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CustomPlaceAddResponse(customPlaceService.addCustomPlace(addRequest, loginMember)));
    }

    @GetMapping("/api/customPlace")
    public ResponseEntity<List<CustomPlaceListFindResponse>> findCustomPlaceListByLoginUserId(HttpServletRequest request) {
        Member loginMember = memberService.getLoginMember(request);
        List<CustomPlace> customPlaceList = customPlaceService.findCustomPlaceListByUserId(loginMember.getId());
        return ResponseEntity.ok()
                .body(customPlaceList.stream().map(CustomPlaceListFindResponse::new).toList());
    }

    @PutMapping("/api/customPlace")
    public ResponseEntity<CustomPlaceAddResponse> updateCustomPlace(@RequestBody CustomPlaceUpdateRequest updateRequest, HttpServletRequest request) {
        Member loginMember = memberService.getLoginMember(request);
        return ResponseEntity.ok()
                .body(new CustomPlaceAddResponse(customPlaceService.updateCustomPlace(updateRequest, loginMember)));
    }

    @DeleteMapping("/api/customPlace")
    public ResponseEntity<String> deleteCustomPlace(@RequestParam Long placeId, HttpServletRequest request) {
        Member loginMember = memberService.getLoginMember(request);
        try {
            customPlaceService.deleteCustomPlace(placeId, loginMember);
            return ResponseEntity.ok()
                    .body("성공");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("권한 오류");
        }
    }
}
