package com.Omok.controller;

import com.Omok.dto.UserSignupRequestDTO;
import com.Omok.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Security 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody UserSignupRequestDTO userSignupRequestDTO) { //@Valid
        userService.signup(userSignupRequestDTO);
        return ResponseEntity.ok().build();
    }
}
