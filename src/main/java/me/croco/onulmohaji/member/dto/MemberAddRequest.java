package me.croco.onulmohaji.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.util.Authorities;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberAddRequest {

    private String email;

    private String nickname;

    private String password;

    private Long localcodeId;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .localcodeId(localcodeId)
                .authorities(Authorities.ROLE_USER)
                .build();
    }
}
