package me.croco.onulmohaji.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.member.domain.MemberSearchInfo;
import me.croco.onulmohaji.member.dto.MemberAddRequest;
import me.croco.onulmohaji.member.dto.MemberSearchInfoFindResponse;
import me.croco.onulmohaji.member.dto.MemberSearchInfoUpdateRequest;
import me.croco.onulmohaji.member.repository.MemberRepository;
import me.croco.onulmohaji.member.repository.MemberSearchInfoRepository;
import me.croco.onulmohaji.route.repository.RouteRepository;
import me.croco.onulmohaji.util.Authorities;
import me.croco.onulmohaji.util.HttpHeaderChecker;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberSearchInfoRepository memberSearchInfoRepository;
    private final HttpHeaderChecker httpHeaderChecker;
    private final RouteRepository routeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email : " + username));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id : " + id));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email : " + email));
    }

    public Long saveMember(MemberAddRequest request) {

        if(memberRepository.existsByEmail(request.getEmail())) {    // 이메일 중복확인
            return null;
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return memberRepository.save(
                        Member.builder()
                                .email(request.getEmail())
                                .nickname(request.getNickname())
                                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                                .localcodeId(request.getLocalcodeId())
                                .authorities(Authorities.ROLE_USER)
                                .build()
                )
                .getId();
    }

    public MemberSearchInfoFindResponse findMemberSearchInfo(Long memberId) {
        MemberSearchInfo memberSearchInfo = memberSearchInfoRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID"));
        String defaultDateValue = null;

        if (memberSearchInfo.getDefaultDate().equals("route")) {    // 날짜 기본값이 가까운 미래 일정일 경우
            defaultDateValue = routeRepository.findNearestRouteDateByUserId(memberId);
        }

        if(defaultDateValue == null) {
            defaultDateValue = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return new MemberSearchInfoFindResponse(memberSearchInfo, defaultDateValue);
    }

    @Transactional
    public void updateMemberSearchInfo(MemberSearchInfoUpdateRequest request) {
        MemberSearchInfo memberSearchInfo = memberSearchInfoRepository.findById(request.getId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원 ID"));

        memberSearchInfo.setDefaultDate(request.getDefaultDate());
        memberSearchInfo.setLocalcodeId(request.getLocalcodeId());
        memberSearchInfo.setDistance(request.getDistance());
        memberSearchInfo.setCategoryFilter(request.getCategoryFilter());
    }

    public Member getLoginMember(HttpServletRequest request) {
        // 로그인 상태인지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            boolean validToken = httpHeaderChecker.checkAuthorizationHeader(request);

            if (!validToken) {   // 비로그인 상태
                return null;
            }
        }

        // 로그인 멤버 반환
        return memberRepository.findByEmail(authentication.getName()).orElse(null);
    }
}