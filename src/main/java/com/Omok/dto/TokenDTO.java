package com.Omok.dto;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseCookie;

/**
 * 클라이언트에 토큰을 보내기 위한 DTO
 * @author rimsong
 * grantType: JWT에 대한 인증 타입. 여기서는 Bearer를 사용한다. 이후 HHTP 헤더에 prefix로 붙여주는 타입
 */

@Builder
@Data
@AllArgsConstructor
public class TokenDTO {

    private String grantType; // JWT에 대한 인증 타입. 여기서는 Bearer를 사용한다. 이후 HHTP 헤더에 prefix로 붙여주는 타입
    private ResponseCookie accessToken;
    private ResponseCookie refreshToken;
}