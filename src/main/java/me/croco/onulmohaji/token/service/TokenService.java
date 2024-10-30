package me.croco.onulmohaji.token.service;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.member.dto.MemberByTokenResponse;
import me.croco.onulmohaji.member.service.MemberService;
import me.croco.onulmohaji.refreshToken.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final MemberService memberService;


    // 리프레시 토큰을 받아 새로운 액세스 토큰 발급
    public String createNewAccessToken(String refreshToken) {

        // 토큰 유효성 검사
        if(!tokenProvider.validToken(refreshToken)) {   // 유효성 검사 실패시
            throw new IllegalArgumentException("토큰 오류");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        Member member = memberService.findById(userId);
        
        return tokenProvider.generateToken(member, Duration.ofHours(2));    // 2시간짜리 토큰 발급
    }

    // 액세스 토큰을 받아 유저 정보 반환
    public MemberByTokenResponse findMemberByAccessToken(String accessToken) {
        Long id = tokenProvider.getUserId(accessToken);
        Member member = memberService.findById(id);
        return new MemberByTokenResponse(member);
    }

}
