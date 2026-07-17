package mst.local.mstsoftware.modules.users.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.services.interfaces.BlacklistServiceInterface;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
@AllArgsConstructor
public class BlacklistRedisService implements BlacklistServiceInterface {

    private final RedisTemplate<String, Object> redis;

    public void revoke(String jti, Instant expiresAt) {
        long ttlSeconds = Duration.between(Instant.now(), expiresAt).getSeconds();
        if (ttlSeconds <= 0) return;
        redis.opsForValue().set(PREFIX + jti, "revoked", ttlSeconds);
    }

    public Boolean isRevoked(String jti) {
        return redis.hasKey(PREFIX + jti);
    }
}
