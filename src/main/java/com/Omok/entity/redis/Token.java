package com.Omok.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "token", timeToLive = 3600 * 24 * 7)
@AllArgsConstructor
@Getter
@ToString
public class Token {
    @Id
    private Long id;
    private String refreshToken;
}