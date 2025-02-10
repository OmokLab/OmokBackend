package com.Omok.global.security;

import com.Omok.global.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화 사용
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // GUEST 인증이 필요한 api
        String[] guestAuthApi = {"/api/guest"};

        // USER 인증이 필요한 api
        String[] userAuthApi = {"/auth/test"};

        // 인증이 필요하지 않은 api
        String[] configApi = {
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/swagger-ui/index.html"
                            };
        String[] companyApi = {};
        String[] imageApi = {};
        String[] userApi = {"/api/member/signup", "/api/member/signin", "/api/oauth2/**"};

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(configApi).permitAll() // configApi에 정의된 경로는 모두 접근 허용
                        .requestMatchers(userApi).permitAll()   // userApi에 정의된 경로는 모두 접근 허용
                        .requestMatchers(userAuthApi).hasRole("USER")
                        .requestMatchers(guestAuthApi).hasRole("GUEST")
                        .anyRequest().authenticated()           // 그 외의 모든 요청은 인증 필요
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않는 Stateless 정책 설정
                )
                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
