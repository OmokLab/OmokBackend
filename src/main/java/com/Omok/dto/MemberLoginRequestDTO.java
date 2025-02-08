package com.Omok.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDTO {
    @NotBlank(message = "Username or email is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
