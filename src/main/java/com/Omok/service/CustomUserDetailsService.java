package com.Omok.service;
import com.Omok.entity.Member;
import com.Omok.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

//    // UserRepository는 사용자 정보를 DB에서 조회하는 JpaRepository 또는 DAO
//    public CustomUserDetailsService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // User 객체를 Spring Security의 UserDetails로 변환
        return createUser(member);
    }

    private User createUser(Member member) {

        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getRole()));

        return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
    }

}
