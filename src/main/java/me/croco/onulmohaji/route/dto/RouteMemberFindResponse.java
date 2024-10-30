package me.croco.onulmohaji.route.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.member.domain.Member;

@Setter
@Getter
public class RouteMemberFindResponse {
    private Long id;
    private String nickname;

    public RouteMemberFindResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
