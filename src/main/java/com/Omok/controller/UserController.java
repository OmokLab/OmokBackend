package com.Omok.controller;

import com.Omok.dto.UserLoginRequestDTO;
import com.Omok.dto.UserSignupRequestDTO;
import com.Omok.global.security.JwtTokenProvider;
import com.Omok.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "Security 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody UserSignupRequestDTO userSignupRequestDTO) { //@Valid
        userService.signup(userSignupRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Security 로그인")
    @PostMapping("/signin")
    public  ResponseEntity<?> loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
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

    @Operation(summary = "Guest 로그인")
    @PostMapping("/guestlogin")
    public ResponseEntity<?> guestLogin() {
        try {
            String guestId = UUID.randomUUID().toString();
            String role = "GUEST";
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,jwtTokenProvider.createToken(guestId, role))
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }


    @Operation(summary = "Guest 테스트")
    @PostMapping("/guest/test")
    public ResponseEntity<?> guestTest() {
        return ResponseEntity.ok().build();
    }
}
