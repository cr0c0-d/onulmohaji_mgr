package me.croco.onulmohaji.localcode.controller;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.localcode.domain.Localcode;
import me.croco.onulmohaji.localcode.service.LocalcodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocalcodeController {

    private final LocalcodeService localcodeService;

    @GetMapping("/api/localcode")
    public ResponseEntity<List<Localcode>> findLocalcodeList() {
        return ResponseEntity.ok()
                .body(localcodeService.findLocalcodeList());
    }
}
