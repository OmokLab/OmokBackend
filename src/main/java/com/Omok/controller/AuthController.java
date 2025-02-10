package com.Omok.controller;

import com.Omok.dto.MemberLoginRequestDTO;
import com.Omok.dto.TokenDTO;
import com.Omok.global.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Security 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(memberLoginRequestDTO.getUsername(), memberLoginRequestDTO.getPassword()));
            Map<String, ResponseCookie> jwtCookie = jwtTokenProvider.generateAccessToken(authentication);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,jwtCookie.get("accessToken").toString())
                    .header(HttpHeaders.SET_COOKIE,jwtCookie.get("refreshToken").toString())
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @Operation(summary = "Auth ROLE_USER 테스트")
    @PostMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().build();
    }
}
