package mst.local.mstsoftware.modules.user.services.impl;

import lombok.RequiredArgsConstructor;
import mst.local.mstsoftware.modules.user.entities.Permission;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import mst.local.mstsoftware.modules.user.repositories.RoleRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionCacheService {

    private final RoleRepository roleRepository;
    private final RedisTemplate<String, String> redis;
    private static final String KEY_PREFIX = "permissions:role:";
    private static final Duration TTL = Duration.ofMinutes(5); // cache 5 phút

    public Set<String> getPermissionsByRoles(List<RoleType> roleNames) {
        Set<String> result = new HashSet<>();

        for (RoleType roleName : roleNames) {
            String cacheKey = KEY_PREFIX + roleName;

            // check cache
            Set<String> cached = redis.opsForSet().members(cacheKey);

            // cache hit
            if (cached != null && !cached.isEmpty()) {
                result.addAll(cached);
                continue;
            }

            // cache miss thì query xuống DB
            Set<String> permissions = roleRepository.findByNameWithPermissions(roleName)
                    .map(role -> role.getPermissions().stream()
                            .map(Permission::toAuthority)
                            .collect(Collectors.toSet())
                    ).orElse(Set.of());

            // set cache
            if (!permissions.isEmpty()) {
                redis.opsForSet().add(cacheKey, permissions.toArray(new String[0]));
                redis.expire(cacheKey, TTL);
            }
            result.addAll(permissions);
        }

        return result;
    }

}
