package me.croco.onulmohaji.token.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessTokenCreateRequest {
    private String refreshToken;
}
