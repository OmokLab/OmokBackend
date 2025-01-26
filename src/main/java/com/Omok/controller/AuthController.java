package com.Omok.controller;

import com.Omok.dto.UserLoginRequestDTO;
import com.Omok.global.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Security 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword()));
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,jwtTokenProvider.createToken(authentication.getName(), "USER"))
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
