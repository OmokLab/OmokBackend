package com.Omok.controller;

import com.Omok.dto.UserLoginRequestDTO;
import com.Omok.global.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
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
    public String login(@RequestBody UserLoginRequestDTO UserLoginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(UserLoginRequestDTO.getUsername(), UserLoginRequestDTO.getPassword()));
            return jwtTokenProvider.createToken(authentication.getName(), "USER");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
