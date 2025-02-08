package com.Omok.service;

import com.Omok.dto.MemberLoginRequestDTO;
import com.Omok.dto.MemberSignupRequestDTO;
import com.Omok.dto.TokenDTO;
import com.Omok.entity.Member;
import com.Omok.global.exception.custom.UserAlreadyExistsException;
import com.Omok.global.security.JwtTokenProvider;
import com.Omok.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public void signup(MemberSignupRequestDTO memberSignupRequestDTO){
        if (memberRepository.existsByEmail(memberSignupRequestDTO.getEmail())
                || memberRepository.existsByUsername(memberSignupRequestDTO.getUsername())) {
            // 둘 중하나가 중복이라면
            throw new UserAlreadyExistsException("아이디나 이메일이 이미 존재합니다.");
        }
        memberSignupRequestDTO = MemberSignupRequestDTO.builder()
                .username(memberSignupRequestDTO.getUsername())
                .email(memberSignupRequestDTO.getEmail())
                .password(passwordEncoder.encode(memberSignupRequestDTO.getPassword()))
                .build();
        Member member = new Member(memberSignupRequestDTO);
        memberRepository.save(member);
    }

    public TokenDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        try {
            // 사용자 인증 수행
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            memberLoginRequestDTO.getUsername(),
                            memberLoginRequestDTO.getPassword()
                    )
            );

            // JWT 토큰 생성
            return jwtTokenProvider.generateAccessToken(authentication);

        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        } catch (DisabledException e) {
            throw new IllegalStateException("비활성화된 계정입니다.");
        } catch (LockedException e) {
            throw new IllegalStateException("계정이 잠겨 있습니다.");
        } catch (AuthenticationException e) {
            throw new RuntimeException("로그인 실패. 다시 시도해주세요.");
        }
    }
}
