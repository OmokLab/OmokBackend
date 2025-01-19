package com.Omok.global.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal; // 인증된 사용자 정보
    private String token; // JWT 토큰

    // 인증 전에 사용하는 생성자
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false); // 인증되지 않은 상태
    }

    // 인증 후 사용하는 생성자
    public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true); // 인증된 상태
    }

    @Override
    public Object getCredentials() {
        return token; // 자격 증명(JWT 토큰)
    }

    @Override
    public Object getPrincipal() {
        return principal; // 사용자 정보
    }
}
