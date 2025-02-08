package com.Omok.entity;

import com.Omok.dto.MemberSignupRequestDTO;
import com.Omok.entity.enums.Platform;
import com.Omok.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id", columnDefinition = "CHAR(36)")
    private String memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
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

    public Member(MemberSignupRequestDTO memberSignupRequestDTO) {
        this.email = memberSignupRequestDTO.getEmail();
        this.username = memberSignupRequestDTO.getUsername();
        this.password = memberSignupRequestDTO.getPassword();
        this.platform = Platform.SERVER;
        this.role = Role.ROLE_USER;
//        this.roles = new ArrayList<>();
//        this.addRole("USER");
    }

    @PrePersist
    public void prePersist() {
        if (this.memberId == null) {
            this.memberId = UUID.randomUUID().toString(); // UUID를 문자열로 변환
        }
    }

//   public void addRole(String role) {
//        this.roles.add(new MemberRoles(this, role));
//    }
}
