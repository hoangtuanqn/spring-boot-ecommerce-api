package mst.local.mstsoftware.modules.users.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.authentication.BadCredentialsException;
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
    public IssuedToken issueRefreshToken(Long userId) {
        String token = jwtService.generateRefreshTokenRaw();
        String tokenHash = utils.hash(token);
        RefreshToken entity = RefreshToken.builder()
                .tokenHash(tokenHash) // token đã hash
                .userId(userId)
                .expiryDate(Instant.now().plus(JwtConfig.getRefreshTokenTTLDays(), ChronoUnit.DAYS))
                .revoked(false)
                .build();
        repository.save(entity);

        return new IssuedToken(token, entity.getId());
    }

    @Override
    public RefreshResult rotateToken(String rawToken) {
        RefreshToken existing = repository.findByTokenHash(utils.hash(rawToken)).orElseThrow(() -> new BadCredentialsException("Refresh token không hợp lệ!"));
        if(existing.isRevoked()) {
            // revoked hết tất cả những refresh token của người dùng
            repository.revokeAllRefreshTokenByUser(existing.getUserId());
            log.error("Phát hiện token bị đánh cắp, đã tiến hành revoke tất cả token của người dùng.");
            throw new SecurityException("Phát hiện token bị đánh cắp, đã tiến hành revoke tất cả token của người dùng.");
        }
        if (existing.getExpiryDate().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token đã hết hạn.");
        }
        existing.setRevoked(true);
        IssuedToken newToken = this.issueRefreshToken(existing.getUserId());
        existing.setReplacedByTokenId(newToken.tokenId());
        repository.save(existing);
        return new RefreshResult(existing.getUserId(), newToken.rawToken());
    }

}