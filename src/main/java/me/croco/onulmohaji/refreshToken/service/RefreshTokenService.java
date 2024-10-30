package me.croco.onulmohaji.refreshToken.service;

import lombok.RequiredArgsConstructor;

import me.croco.onulmohaji.refreshToken.domain.RefreshToken;
import me.croco.onulmohaji.refreshToken.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("토큰 오류"));
    }
}
