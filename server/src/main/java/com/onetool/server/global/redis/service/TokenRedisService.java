package com.onetool.server.global.redis.service;

import com.onetool.server.member.dto.MemberLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenRedisService {

    private final RedisTemplate<String, Object> tokenRedisTemplate;

    public void setValuesWithTimeout(String s, String refreshToken, Long refreshTokenExpirationMillis) {
        ValueOperations<String, Object> values = tokenRedisTemplate.opsForValue();
        values.set(
                s,
                refreshToken,
                Duration.ofSeconds(refreshTokenExpirationMillis)
        );
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = tokenRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteValues(String memberId) {
        ValueOperations<String, Object> values = tokenRedisTemplate.opsForValue();
        values.getAndDelete(memberId);
    }
}
