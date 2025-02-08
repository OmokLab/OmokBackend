package com.Omok.global.security;

import com.Omok.dto.TokenDTO;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    @Value("${jwt.domain}")
    private String domain;

    public TokenDTO generateAccessToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        // 숫자 86400000은 토큰의 유효기간으로 1일을 나타냅니다. 보통 토큰은 30분 정도로 생성하는데 테스트를 위해 1일로 설정했습니다.
        // 1일: 24*60*60*1000 = 86400000
        Date accessTokenExpiresIn = new Date(now + validityInMilliseconds);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        ResponseCookie accessCookie =
                ResponseCookie.from("accessToken")
                        .value(accessToken)
                        .domain(domain)
//                        .secure(true) //https 에서만 쿠키전송
//                        .path("/")
                        .maxAge(validityInMilliseconds / 1000)
                        .httpOnly(true)
                        .build();
        ResponseCookie refreshCookie =
                ResponseCookie.from("refreshToken")
                        .value(refreshToken)
                        .domain(domain)
//                        .secure(true) //https 에서만 쿠키전송
//                        .path("/")
                        .maxAge(validityInMilliseconds / 1000)
                        .httpOnly(true)
                        .build();

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessCookie)
                .refreshToken(refreshCookie)
                .build();
    }
    // JWT 생성
//    public String createToken(Authentication authentication) {
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
////        Claims claims = Jwts.claims().setSubject(username);
////        claims.put("role", role); // 사용자 역할 추가
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//        String jwt = Jwts.builder()
//                .claim(AUTHORITIES_KEY,authorities)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//
//        return ResponseCookie.from("accessToken")
//                .value(jwt)
//                .build().toString();
//    }

    // JWT에서 사용자명 추출
    public String getUsername(String token) { //수정 필
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Cookie toServletCookie(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setDomain(domain);
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setSecure(responseCookie.isSecure());
        return cookie;
    }
}
