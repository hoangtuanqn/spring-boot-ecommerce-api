package mst.local.mstsoftware.modules.users.services.impl;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.entities.BlacklistedToken;
import mst.local.mstsoftware.modules.users.repositories.BlacklistedTokenRepository;
import mst.local.mstsoftware.services.JwtService;

@Service
@Slf4j
@AllArgsConstructor
public class BlacklistService {
    private BlacklistedTokenRepository repository;
    private JwtService jwtService;

    public void blacklistToken(String token) {
        if (isTokenBlackList(token)) {
            return;
        }
        Claims claims = jwtService.extractAllClaims(token);
        Long userId = jwtService.extractUserId(token);
        Date expiration = claims.getExpiration();

        BlacklistedToken entity = new BlacklistedToken();
        entity.setUserId(userId);
        entity.setToken(token);
        // log.info("Origin: " + expiration);
        // log.info("Instant: " + expiration.toInstant());
        // log.info("At zone: " +
        // expiration.toInstant().atZone(ZoneId.systemDefault()));
        entity.setExpiryDate(
                expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        repository.save(entity);
    }

    public boolean isTokenBlackList(String token) {
        return repository.existsByToken(token);
    }

    public void blacklistAllUserTokens(Long userId, List<String> activeTokens) {
        activeTokens.forEach(this::blacklistToken);
    }
}
