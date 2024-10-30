package me.croco.onulmohaji.controller;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.NaverService;
import me.croco.onulmohaji.dto.NaverLocalFindRequest;
import me.croco.onulmohaji.dto.NaverLocalFindResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocalController {

    private final NaverService naverService;

    @GetMapping("/api/local")
    public ResponseEntity<NaverLocalFindResponse> searchLocal(@RequestBody NaverLocalFindRequest request) {
        NaverLocalFindResponse response = naverService.searchLocal(request);
        return ResponseEntity.ok()
                .body(response);
    }
}
