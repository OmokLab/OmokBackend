package com.Omok.entity;

import com.Omok.dto.UserSignupRequestDTO;
import com.Omok.entity.enums.Platform;
import com.Omok.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String email;
    private String username;
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // UserDetails 인터페이스 메서드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRole()));
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }

    public User(UserSignupRequestDTO userSignupRequestDTO) {
        this.email = userSignupRequestDTO.getEmail();
        this.username = userSignupRequestDTO.getUsername();
        this.password = userSignupRequestDTO.getPassword();
        this.platform = Platform.SERVER;
        this.role = Role.ROLE_USER;
    }
}
