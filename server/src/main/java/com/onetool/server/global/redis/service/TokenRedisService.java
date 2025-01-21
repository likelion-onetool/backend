package com.onetool.server.global.redis.service;

import com.onetool.server.global.auth.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenRedisService {

    private final RedisTemplate<String, Object> tokenRedisTemplate;

    public TokenRedisService(
            @Qualifier("tokenRedisTemplate")
            RedisTemplate<String, Object> tokenRedisTemplate) {
        this.tokenRedisTemplate = tokenRedisTemplate;
    }

    public void setValuesWithTimeout(String email, String refreshToken, Long refreshTokenExpirationMillis) {
        ValueOperations<String, Object> values = tokenRedisTemplate.opsForValue();
        values.set(email, refreshToken, Duration.ofSeconds(refreshTokenExpirationMillis));
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = tokenRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteValues(String email) {
        ValueOperations<String, Object> values = tokenRedisTemplate.opsForValue();
        values.getAndDelete(email);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(tokenRedisTemplate.hasKey(key));
    }
}