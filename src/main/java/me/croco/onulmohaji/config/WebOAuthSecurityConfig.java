package me.croco.onulmohaji.config;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.member.service.MemberService;
import me.croco.onulmohaji.refreshToken.repository.RefreshTokenRepository;
import me.croco.onulmohaji.util.HttpHeaderChecker;
import me.croco.onulmohaji.token.service.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final HttpHeaderChecker httpHeaderChecker;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Value("${onulmohaji.croco.front}")
    private String origin;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/error", "/static/**", "/favicon.ico");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/signup", "/login", // 로그인
                                        "/api/token", // 토큰 발급
                                        "/api/token/**", // 토큰 하위
                                        "/api/festival",    // 축제
                                        "/api/festival/**",  // 축제 하위
                                        "/api/popup",   // 팝업스토어
                                        "/api/popup/**",   // 팝업스토어 하위
                                        "/api/exhibition",   // 전시회
                                        "/api/exhibition/**",   // 전시회 하위
                                        "/api/localcode", // 지역코드 조회
                                        "/api/facility",    // 시설
                                        "/api/facility/**"  // 시설 하위
                                ).permitAll()

                                .requestMatchers(
                                        new AntPathRequestMatcher("/api/articles/**", HttpMethod.GET.name()), // GET 요청은 모두 허용
                                        new AntPathRequestMatcher("/api/members/", HttpMethod.POST.name()),  // 회원가입만 허용
                                        new AntPathRequestMatcher("/api/route/permission/**", HttpMethod.GET.name())    // route permission route정보 조회 허용
                                ).permitAll()

                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()

                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                        .clearAuthentication(true)
                )

                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/loginProcessing")
                                .successHandler(customAuthenticationSuccessHandler())
                                .failureHandler(customAuthenticationFailureHandler)
                                .usernameParameter("email")
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.disable());


        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
//        http.authorizeHttpRequests((authorize) ->
//                authorize.requestMatchers("/api/token", "/signup").permitAll()
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().permitAll()
//                );

        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/login")

                //Authorization 요청과 관련된 상태 저장
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                )

                .successHandler(customAuthenticationSuccessHandler())
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(oAuth2UserCustomService)
                )

                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserCustomService))

        );

        http.logout(logout->logout
                .logoutSuccessUrl("/login")
        );

        // api로 시작하는 url인 경우 401 상태코드를 반환하도록 예외 처리
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"))
        );


        return http.build();

    }


//    @Bean
//    public OAuth2SuccessHandler oAuth2SuccessHandler() {
//        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), memberService);
//    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, MemberService memberService) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        managerBuilder.userDetailsService(memberService)
                .passwordEncoder(bCryptPasswordEncoder);

        return managerBuilder.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(httpHeaderChecker);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), memberService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // 허용할 오리진 설정
        configuration.addAllowedOrigin("http://25.10.86.27:3000");
        configuration.addAllowedOrigin("http://192.168.0.2:3000");
        //configuration.addAllowedOrigin(origin);
        configuration.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        configuration.addAllowedHeader("Content-Type"); // 헤더 허용
        configuration.addAllowedHeader("Authorization"); // 헤더 허용
        configuration.setAllowCredentials(true); // 쿠키 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }



}
