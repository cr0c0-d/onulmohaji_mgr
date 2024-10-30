package me.croco.onulmohaji.config.auth;

import lombok.Builder;
import lombok.Getter;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.util.Authorities;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        //this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" ->
                 ofGoogle(userNameAttributeName, attributes);

            case "naver" ->
                ofNaver(userNameAttributeName, (LinkedHashMap) attributes.get("response"));

            case "kakao" ->
                ofKakao(userNameAttributeName, attributes);

            default -> null;
        };

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("name"))
                .email("google_" + (String) attributes.get("email"))
                //.picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("nickname"))
                .email("naver_" + (String) attributes.get("id"))
                //.picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) ((LinkedHashMap) attributes.get("properties")).get("nickname"))
                .email("kakao_" + attributes.get("id"))
                //.picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                //.picture(picture)
                .authorities(Authorities.ROLE_USER)
                .build();
    }
}