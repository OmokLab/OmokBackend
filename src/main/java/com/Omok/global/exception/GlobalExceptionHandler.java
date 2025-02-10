package com.Omok.global.exception;

import com.Omok.global.exception.custom.UserAlreadyExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) { // secucrity 필터에서 발생한 exception은 controller 오기 이전에 처리됨
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "token_expired");
        errorResponse.put("message", "토큰이 만료되었습니다. 다시 로그인하거나 refresh token을 사용해 토큰을 갱신해주세요.");
        // HTTP 401 Unauthorized 상태 코드와 함께 응답합니다.
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<?> handleDuplicateKeyException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

//    // 로그인 정보가 없을 때, 200번대와 함께 loginInfoDto 반환
//    @ExceptionHandler(LoginInfoNotFoundException.class)
//    protected ResponseEntity<MemberResponseDto> handleDuplicateKeyException(LoginInfoNotFoundException e) {
//        return ResponseEntity.ok().body(e.getMemberResponseDto());
//    }
//
//    // OAuth2 로그인 시, 해당 이메일로 가입된 멤버가 다른 플랫폼일 때
//    @ExceptionHandler(OAuth2UserAlreadyException.class)
//    protected ResponseEntity<?> handleDuplicateKeyException(OAuth2UserAlreadyException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//    }

    // Spring 이미지 업로드 용량 제한
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지 용량을 초과했습니다.");
    }
}
