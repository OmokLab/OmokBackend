package com.Omok.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    GOOGLE("GOOGLE"),
    SERVER("SERVER");

    private final String platform;
}