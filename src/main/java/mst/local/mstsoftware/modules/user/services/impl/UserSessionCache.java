package mst.local.mstsoftware.modules.user.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserSessionCache {

    private final RedisTemplate<String, String> redis;
    private final ObjectMapper objectMapper;
    private static final String KEY_PREFIX = "user:session:";

    public void set(Long userId, String email, Set<RoleType> roles, Duration ttl) {
        String key = KEY_PREFIX + userId;
        Map<String, Object> session = Map.of(
                "email", email,
                "roles", roles
        );

        try {
            redis.opsForValue().set(key, objectMapper.writeValueAsString(session), ttl);
        } catch (Exception e) {
            throw new RuntimeException("Không thể lưu session vào redis!");
        }
    }

    public Map<String, Object> get(Long userId) {
        String key = KEY_PREFIX + userId;
        String raw = redis.opsForValue().get(key);
        if (raw == null) return null;
        try {
            return objectMapper.readValue(raw, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void evict(Long userId) {
        redis.delete(KEY_PREFIX + userId);
    }

}
