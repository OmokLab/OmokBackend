package com.Omok.dto;

import com.Omok.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupRequestDTO {
    @NotBlank(message = "Username or email is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    public MemberSignupRequestDTO(Member member) {
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.email = member.getEmail();
    }
}
