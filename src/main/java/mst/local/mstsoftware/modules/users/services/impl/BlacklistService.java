package mst.local.mstsoftware.modules.users.services.impl;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.helpers.TokenHashUtil;
import mst.local.mstsoftware.modules.users.entities.BlacklistedToken;
import mst.local.mstsoftware.modules.users.repositories.BlacklistedTokenRepository;
import mst.local.mstsoftware.modules.users.services.interfaces.BlacklistServiceInterface;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BlacklistService implements BlacklistServiceInterface {
    private BlacklistedTokenRepository repository;
    private JwtServiceInterface jwtService;
    private TokenHashUtil tokenHashUtil;

    @Override
    @Transactional
    public void blacklistToken(String token) {
        if (isTokenBlackList(token)) {
            return;
        }
        String tokenHash = tokenHashUtil.hash(token);
        Claims claims = jwtService.extractAllClaims(token);
        Long userId = jwtService.extractUserId(token);
        Date expiration = claims.getExpiration();

        BlacklistedToken entity = new BlacklistedToken();
        entity.setUserId(userId);
        entity.setTokenHash(tokenHash);
        entity.setExpiryDate(
                expiration.toInstant().atZone(ZoneId.systemDefault()).toInstant());

        repository.save(entity);
    }

    @Override
    public boolean isTokenBlackList(String token) {
        String tokenHash = tokenHashUtil.hash(token);
        return repository.existsByTokenHash(tokenHash);
    }

    @Override
    @Transactional
    public void blacklistAllUserTokens(Long userId, List<String> activeTokens) {
        activeTokens.forEach(this::blacklistToken);
    }
}
