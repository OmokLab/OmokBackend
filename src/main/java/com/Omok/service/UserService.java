package com.Omok.service;

import com.Omok.dto.UserSignupRequestDTO;
import com.Omok.entity.User;
import com.Omok.global.exception.custom.UserAlreadyExistsException;
import com.Omok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignupRequestDTO userSignupRequestDTO){
        if (userRepository.existsByEmail(userSignupRequestDTO.getEmail())
                || userRepository.existsByUsername(userSignupRequestDTO.getUsername())) {
            // 둘 중하나가 중복이라면
            throw new UserAlreadyExistsException("아이디나 이메일이 이미 존재합니다.");
        }
        userSignupRequestDTO = UserSignupRequestDTO.builder()
                .username(userSignupRequestDTO.getUsername())
                .email(userSignupRequestDTO.getEmail())
                .password(passwordEncoder.encode(userSignupRequestDTO.getPassword()))
                .build();
        userRepository.save(new User(userSignupRequestDTO));
    }
}
