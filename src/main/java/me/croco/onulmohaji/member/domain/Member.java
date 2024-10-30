package me.croco.onulmohaji.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.croco.onulmohaji.util.Authorities;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "localcode_id")
    private Long localcodeId;

    @Enumerated(EnumType.STRING)
    private Authorities authorities;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Builder
    public Member(String email, String nickname, String password, Long localcodeId, Authorities authorities) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.localcodeId = localcodeId;
        this.authorities = authorities;
    }


    // 사용자 닉네임 변경
    public Member update(String nickname) {
        this.nickname = nickname;

        return this;
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    // 사용자명(이메일) 반환
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자 비밀번호 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료 여부 확인 로직
        return true;    // true : 만료되지 않음
    }

    // 계정 잠금여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;    // true : 잠금되지 않음
    }

    // 비밀번호 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;    // true : 만료되지 않음
    }

    // 계정 사용가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;    // true : 사용가능
    }
}