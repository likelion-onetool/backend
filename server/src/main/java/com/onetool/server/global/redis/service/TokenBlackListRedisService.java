package com.onetool.server.global.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenBlackListRedisService {

    private final RedisTemplate<String, Object> tokenBlackListRedisTemplate;

    public TokenBlackListRedisService(
            @Qualifier("tokenBlackListRedisTemplate")
            RedisTemplate<String, Object> tokenBlackListRedisTemplate
    ) {
        this.tokenBlackListRedisTemplate = tokenBlackListRedisTemplate;
    }

    public void setBlackList(String accessToken, Long expiration) {
        tokenBlackListRedisTemplate.opsForValue()
                .set(accessToken, "BlackList", expiration, TimeUnit.MINUTES);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = tokenBlackListRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteValues(String memberId) {
        ValueOperations<String, Object> values = tokenBlackListRedisTemplate.opsForValue();
        values.getAndDelete(memberId);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(tokenBlackListRedisTemplate.hasKey(key));
    }
}
