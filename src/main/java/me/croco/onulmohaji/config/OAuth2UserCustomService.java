package me.croco.onulmohaji.config;

import lombok.RequiredArgsConstructor;

import me.croco.onulmohaji.config.auth.CustomOAuth2User;
import me.croco.onulmohaji.config.auth.OAuthAttributes;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.member.repository.MemberRepository;
import me.croco.onulmohaji.util.Authorities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Value("${default.profileImg}")
    private String DEFAULT_PROFILE_IMAGE;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 4. 유저 정보 dto 생성
        OAuthAttributes oAuth2UserInfo = OAuthAttributes.of(registrationId, userNameAttributeName, user.getAttributes());

        // 5. 회원가입 및 로그인
        Member member = saveOrUpdate(oAuth2UserInfo, registrationId);

        // 6. OAuth2User로 반환
        return new CustomOAuth2User(registrationId, user.getAttributes(), (List<GrantedAuthority>) user.getAuthorities().stream().toList(), member.getEmail());
    }

    private Member saveOrUpdate(OAuthAttributes oAuth2UserInfo, String registrationId) {

        Optional<Member> member = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        return memberRepository.save(
                    member.orElseGet(() ->
                            Member.builder()
                                .email(oAuth2UserInfo.getEmail())
                                .nickname(oAuth2UserInfo.getNickname())
                                .password("oAuth2")
                                .authorities(Authorities.ROLE_USER)
                                //.profileImg(DEFAULT_PROFILE_IMAGE)
                                .build())
                    .update(oAuth2UserInfo.getNickname())
        );
    }
}
