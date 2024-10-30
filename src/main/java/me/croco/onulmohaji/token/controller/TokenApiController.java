package me.croco.onulmohaji.token.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.config.CustomAuthenticationSuccessHandler;
import me.croco.onulmohaji.member.dto.MemberByTokenResponse;
import me.croco.onulmohaji.token.dto.AccessTokenCreateResponse;
import me.croco.onulmohaji.token.service.TokenService;
import me.croco.onulmohaji.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenCreateResponse> createNewAccessToken(HttpServletRequest request) {
        String refresh_token = CookieUtil.getCookie(CustomAuthenticationSuccessHandler.REFRESH_TOKEN_COOKIE_NAME, request);
        try {
            String newAccessToken = tokenService.createNewAccessToken(refresh_token);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AccessTokenCreateResponse(newAccessToken));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/token/{accessToken}")
    public ResponseEntity<MemberByTokenResponse> findMemberByToken(@PathVariable String accessToken) {
        return ResponseEntity.ok()
                .body(tokenService.findMemberByAccessToken(accessToken));
    }
}
