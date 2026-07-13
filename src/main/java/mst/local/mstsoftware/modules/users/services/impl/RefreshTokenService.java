package mst.local.mstsoftware.modules.users.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.entities.RefreshToken;
import mst.local.mstsoftware.modules.users.repositories.RefreshTokenRepository;
import mst.local.mstsoftware.modules.users.services.interfaces.RefreshTokenServiceInterface;
import mst.local.mstsoftware.services.JwtService;
import mst.local.mstsoftware.config.JwtConfig;
import mst.local.mstsoftware.helpers.TokenHashUtil;

@Service
@Slf4j
@AllArgsConstructor
public class RefreshTokenService implements RefreshTokenServiceInterface {
    private JwtService jwtService;
    private RefreshTokenRepository repository;
    private JwtConfig JwtConfig;
    private TokenHashUtil utils;

    @Override
    public String issueRefreshToken(Long userId) {
        String token = jwtService.generateRefreshTokenRaw();
        String tokenHash = utils.hash(token);
        RefreshToken entity = RefreshToken.builder()
                .tokenHash(tokenHash)
                .userId(userId)
                .expiryDate(Instant.now().plus(JwtConfig.getRefreshTokenTTLDays(), ChronoUnit.DAYS))
                .revoked(false)
                .build();
        repository.save(entity);

        return token;
    }

    @Override
    public RefreshResult rotateToken(String rawToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rotateToken'");
    }

}
