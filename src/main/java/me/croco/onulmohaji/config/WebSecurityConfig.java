package me.croco.onulmohaji.config;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/signup").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.loginPage("/login")
                                .defaultSuccessUrl("/search")
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}