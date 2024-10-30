package me.croco.onulmohaji.member.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverLoginRequest {
    String url;
    String state;

    public NaverLoginRequest(String url, String state) {
        this.url = url;
        this.state = state;
    }
}
